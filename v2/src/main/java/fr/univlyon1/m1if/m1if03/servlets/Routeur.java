/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletConfig;
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
@WebServlet(name = "routeur", urlPatterns = {"/*"})
public class Routeur extends HttpServlet {

    /**
     * Initialisation du modèle
     */
    @Override
    public void init( ServletConfig sc) {
        sc.getServletContext().setAttribute("groupes", new HashMap<String, Groupe>());
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri= request.getRequestURI();
        String[] uriSplit = uri.split("/");
        
        switch (uriSplit[0]) {
            case "users":
                //Controller User
                break;
            case "groupes":
                uriGroupes(uriSplit, request, response);
                break;
            default:
                // error
                break;
        }
    }

    private void uriGroupes(String[] uriSplit, HttpServletRequest request, HttpServletResponse response) {
        if (uriSplit.length == 1) {
            //listes de groupes : /groupes
            // controller groupes
            return;
        }
        if (uriSplit.length == 2) {
            //Detail d'un groupe : /groupes/<id = split[1]>
            // controller détail groupe
            return;
        } 
        if (uriSplit[2].equals("billets")) {
            uriBillets(uriSplit, request, response);     
        } else {
            //error
        }
    }

    private void uriBillets(String[] uriSplit, HttpServletRequest request, HttpServletResponse response) {
        if (uriSplit.length == 3) {
            //listes des billets : /groupes/<id = split[1]>/billets
            // controller des billets
            return;
        }
        if (uriSplit.length == 4) {
            //Detail d'un groupe : /groupes/<id = split[1]>/billets/<id = split[3]>
            // controller détail billet
            return;
        } 
        if (uriSplit[4].equals("comments")) {
            uriComments(uriSplit, request, response);
        } else{
            //error
        }

    }

    private void uriComments(String[] uriSplit, HttpServletRequest request, HttpServletResponse response) {
        if (uriSplit.length == 5) {
            //listes des commantaire : /groupes/<id = split[1]>/billets/<id = split[3]>/comments
            // controller des commentaires
            return;
        }
        if (uriSplit.length == 6) {
            //Detail d'un groupe : /groupes/<id = split[1]>/billets/<id = split[3]>/comments/<id = split[5]>
            // controller détail d'un commentaire
            return;
        }
        //error

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
