package fr.univlyon1.m1if.m1if03.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "commente", urlPatterns = "/commente")
public class Commente extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String com = request.getParameter("commentaire");
        
        if(com != null && !com.equals("")) {
            request.getRequestDispatcher("billet").forward(request, response);
        } else {
            response.sendRedirect("billets");
        }
    }
}
