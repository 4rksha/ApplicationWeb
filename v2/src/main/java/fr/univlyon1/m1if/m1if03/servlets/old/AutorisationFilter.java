/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.old;

import fr.univlyon1.m1if.m1if03.classes.Groupe;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author vasli
 */
@WebFilter("/AutorisationFilter")
public class AutorisationFilter implements Filter{

    private ServletContext context;//utilisé pour de la log;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String chemin = request.getRequestURI();
        HttpSession session = request.getSession(false);
        
        // Accés à un billet, on vérifie si le billet indiqué existe
        if (request.getMethod().equals("GET") && chemin.equals("/billet")) {
            if (request.getParameter("id") != null) { 
                String nameGroupe = (String) session.getAttribute("groupe");
                Map<String,Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
                
                if (modele != null
                        && modele.get(nameGroupe) != null
                        && modele.get(nameGroupe).getgBillets() != null
                        && modele.get(nameGroupe).getgBillets().getBillet(new Integer(request.getParameter("id"))) != null
                ){
                    this.context.log("Le billet existe avec le groupe de l'utilisateur");
                    chain.doFilter(request, response);
                }else{
                    this.context.log("Le billet n'est pas accessible à l'utilisateur");
                    request.getRequestDispatcher("/billets").forward(request, response);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {}
    
}

