/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets;

import java.io.IOException;
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
@WebFilter("/AuthentificationFilter")
public class AuthentificationFilter implements Filter{

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
        // on regarde si la requete contient une session
        if (request.isRequestedSessionIdValid()) {
            this.context.log("User connecté ".concat(request.getRequestURI()));
            chain.doFilter(request, response);
        } else {
            String pseudo = request.getParameter("pseudo");
            String groupe = request.getParameter("groupe");
            // Conexxion d'un utilisateur
            if (request.getMethod().equals("POST") 
                    && pseudo != null 
                    && !pseudo.equals("")
                    && groupe != null 
                    && !groupe.equals("")
            ) {
                HttpSession session = request.getSession(true);
                session.setAttribute("pseudo", pseudo);
                session.setAttribute("groupe", groupe);
                this.context.log("Nouvelle connexion ".concat(request.getRequestURI()));
                request.getRequestDispatcher("/billets").forward(request, response);
            } else {
                // La connexion a echouée
                request.getRequestDispatcher("index.html").forward(request, response);
            }
        }
    }

    @Override
    public void destroy() {}
    
}
