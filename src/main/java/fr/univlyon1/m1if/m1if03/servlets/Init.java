package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Groupe;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletConfig;

@WebServlet(name = "Init", urlPatterns = "/Init")
public class Init extends HttpServlet {
    
    
    @Override
    public void init( ServletConfig sc) {
//        sc.getServletContext().setAttribute("groupes", new HashMap<String, Groupe>());
    }
    
    /**
     * Gère les requêtes POST sur /Init
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pseudo = request.getParameter("pseudo");
        String groupe = request.getParameter("groupe");
        if(pseudo != null && !pseudo.equals("")
                && groupe != null && !groupe.equals("")) {
            HttpSession session = request.getSession(true);
            session.setAttribute("pseudo", pseudo);
            session.setAttribute("groupe", groupe);
            response.sendRedirect("billets");
        } else {
            response.sendRedirect("index.html");
        }
    }

    /**
     * Gère les requêtes GET sur /Init
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String groupe = request.getParameter("group"); // utilisé pour le changement de groupe
        if (groupe != null && session != null) {
            session.setAttribute("groupe", groupe);
            response.sendRedirect("billets");
        } else {
            response.sendRedirect("index.html");
        }
    }
}
