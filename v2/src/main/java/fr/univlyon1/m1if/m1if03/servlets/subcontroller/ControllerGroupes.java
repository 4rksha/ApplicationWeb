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
                Map<String,Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }



}
