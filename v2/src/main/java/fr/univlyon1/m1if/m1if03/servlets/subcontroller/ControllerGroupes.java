/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.subcontroller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.univlyon1.m1if.m1if03.classes.GestionBillets;
import fr.univlyon1.m1if.m1if03.classes.Groupe;

/**
 *
 * @author vasli
 */
@WebServlet(name = "ControllerGroupes", urlPatterns = {"/ControllerGroupes"})
public class ControllerGroupes extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Recupération du modèle
        Map<String,Groupe> modele = (HashMap<String,Groupe>) request.getServletContext().getAttribute("groupes");
        //On charge les listes de les groupes contenue dans le modèles
        request.getServletContext().setAttribute(
            "groupesList", 
            modele.keySet()
        );
        request.getRequestDispatcher("WEB-INF/jsp/groupes.jsp").forward(request, response);           
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Recupération du modèle
        String author = request.getParameter("author");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        System.out.println(request.getParameterMap().keySet());
        System.out.println(title + " " +content+ " " + author+ " ");
        if (author == null || title == null || content == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        Map<String,Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        if(modele.containsKey(title) == false) {
            List<String> liste = new ArrayList<String>();
            GestionBillets gBillet = new GestionBillets();
            Groupe g = new Groupe(
                    title,
                    content,
                    author,
                    liste,
                    gBillet
            );
            modele.put(title, g);
//            System.out.println(modele.keySet());
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



}
