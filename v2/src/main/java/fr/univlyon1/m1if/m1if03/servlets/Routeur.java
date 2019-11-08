/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets;

import java.io.IOException;
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
@WebServlet(name = "routeur", urlPatterns = {"/groupes/*"})
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
        request.getServletContext().setAttribute(
            "URI", 
            uri
        );
        String[] uriSplit = uri.split("/");
        switch (uriSplit[1]) {
//            case "users": // temporaire du à des erreurs de routage
//                //Controller User
//                break;
            case "groupes":
                this.uriGroupes(uriSplit, request, response);
                break;
            default:
                // error
                break;
        }
    }

    private void uriGroupes(String[] uriSplit, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        if (uriSplit.length == 2) {
            //listes de groupes : /groupes
            request.getRequestDispatcher("/ControllerGroupes").forward(request, response);
            return;
        }
        if (uriSplit.length == 3) {
            //Detail d'un groupe : /groupes/<id = split[2]>
            // controller détail groupe
            request.getRequestDispatcher("/ControllerGroupe").forward(request, response);
            return;
        } 
        if (uriSplit[3].equals("billets")) {
            uriBillets(uriSplit, request, response);     
        } else {
            //error
        }
    }

    private void uriBillets(String[] uriSplit, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        if (uriSplit.length == 4) {
            //listes des billets : /groupes/<id = split[2]>/billets
            // controller des billets
            request.getRequestDispatcher("/ControllerBillets").forward(request, response);
            return;
        }
        if (uriSplit.length == 5) {
            //Detail d'un groupe : /groupes/<id = split[2]>/billets/<id = split[4]>
            // controller détail billet
            request.getRequestDispatcher("/ControllerBillet").forward(request, response);
            return;
        } 
        if (uriSplit[5].equals("comments")) {
            uriComments(uriSplit, request, response);
        } else{
            //error
        }

    }

    private void uriComments(String[] uriSplit, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        if (uriSplit.length == 6) {
            //listes des commantaire : /groupes/<id = split[2]>/billets/<id = split[4]>/comments
            // controller des commentaires
            request.getRequestDispatcher("/ControllerComments").forward(request, response);
            return;
        }
        if (uriSplit.length == 7) {
            //Detail d'un groupe : /groupes/<id = split[2]>/billets/<id = split[4]>/comments/<id = split[5]>
            // controller détail d'un commentaire
            request.getRequestDispatcher("/ControllerComment").forward(request, response);
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
