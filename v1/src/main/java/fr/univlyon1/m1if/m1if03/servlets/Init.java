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
    /**
     * Gère les requêtes GET sur /Init
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // On réalise le changement de groupe d'un utilisateur
        HttpSession session = request.getSession(false);
        String groupe = request.getParameter("group"); 
        if (groupe != null && session != null) {
            session.setAttribute("groupe", groupe);
            response.sendRedirect("billets");
        } else {
            response.sendRedirect("index.html");
        }
    }
}
