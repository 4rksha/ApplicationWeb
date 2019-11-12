package fr.univlyon1.m1if.m1if03.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "ContentNegotiation")
public class ContentNegotiation implements Filter {

    FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        chain.doFilter(req, resp);

        ServletContext context = req.getServletContext();

        Object hsr = context.getAttribute("status");
        if (hsr == null) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus((int) hsr);
            context.removeAttribute("status");
        }

        String location = (String) context.getAttribute("Location");
        if (location != null) {
            resp.setHeader("Location", location);
            context.removeAttribute("Location");
        }

        Cookie cookie = (Cookie) context.getAttribute("Cookie");
        if (cookie != null) {
            resp.addCookie(cookie);
            context.removeAttribute("Cookie");
        }

        String token = (String) context.getAttribute("Authorization");
        if (token != null) {
            resp.addHeader("Authorization", token);
            context.removeAttribute("Authorization");
        }

        String view = (String) context.getAttribute("view");
        // S'il y a un contenu Ã  renvoyer
        if (view != null) {

            if (req.getContentType() != null && req.getContentType().equals("application/json")) {
                // "application/json":
                String data = (String) req.getServletContext().getAttribute("data");
                OutputStream out = resp.getOutputStream();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                out.write(data.getBytes());
                out.flush();
            } else if (((HttpServletRequest) req).getHeader("Accept").startsWith("text/html")) {
                // Cas des JSP (nomméees dans le web.xml)
                RequestDispatcher dispatcher = filterConfig.getServletContext().getNamedDispatcher("vue-" + view);
                if (dispatcher != null) {
                    HttpServletRequest wrapped = new HttpServletRequestWrapper((HttpServletRequest) req) {
                        @Override
                        public String getServletPath() {
                            return "";
                        }
                    };
                    dispatcher.forward(wrapped, resp);
                }
            }
            context.removeAttribute("view");
        }
    }

    @Override
    public void destroy() {
    }
}
