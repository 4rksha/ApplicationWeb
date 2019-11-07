/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.subcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.univlyon1.m1if.m1if03.classes.*;

/**
 *
 * @author vasli
 */
@WebServlet(name = "ControllerGroupe", urlPatterns = {"/ControllerGroupe"})
public class ControllerGroupe extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = request.getRequestURI().split("/");
        Map<String,Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        request.getServletContext().setAttribute("groupe", groupe);
        request.getRequestDispatcher("WEB-INF/jsp/groupe.jsp").forward(request, response);   
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = request.getRequestURI().split("/");
        Map<String,Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            if (request.getParameter("author") != null) {
                groupe.setAuteur(request.getParameter("author"));
            } 
            if (request.getParameter("content") != null) {
                groupe.setDescription(request.getParameter("description"));
            } 
            if (request.getParameter("title") != null) {
                groupe.setNom(request.getParameter("title"));
            }
        }
    }
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = request.getRequestURI().split("/");
        Map<String,Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        if (modele.containsKey(uriSplit[2])) {
            modele.remove(uriSplit[2]);
        }
    }


}
