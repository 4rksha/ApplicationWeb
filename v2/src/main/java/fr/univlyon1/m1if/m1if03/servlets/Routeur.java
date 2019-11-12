/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.univlyon1.m1if.m1if03.classes.Groupe;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *
 * @author vasli
 */
@WebServlet(name = "routeur", urlPatterns = {"/*"})
public class Routeur extends HttpServlet {

    /**
     * Initialisation du modèle
     *
     * @param sc
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init(ServletConfig sc) throws ServletException {
        super.init(sc);
        ServletContext context = sc.getServletContext();

        context.setAttribute("groupes", new HashMap<String, Groupe>());
        context.setAttribute("users", new ArrayList<String>());
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
    protected void dispatch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher;

        String uri = request.getRequestURI();
        request.getServletContext().setAttribute("URI", uri);
        String[] uriSplit = uri.split("/");
        if (uriSplit.length > 1) {
            switch (uriSplit[1]) {
                case "users": // temporaire du à des erreurs de routage
                    dispatcher = request.getServletContext().getNamedDispatcher("ControllerUser");
                    dispatcher.include(request, response);
                    break;
                case "groupes":
                    this.uriGroupes(uriSplit, request, response);
                    break;
                default:
                    dispatcher = request.getServletContext().getNamedDispatcher("default");
                    HttpServletRequest wrapped = new HttpServletRequestWrapper(request) {
                        @Override
                        public String getServletPath() {
                            return "";
                        }
                    };
                    dispatcher.forward(wrapped, response);
                    break;
            }
        } else {

        }
    }

    private void uriGroupes(String[] uriSplit, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;

        if (uriSplit.length == 2) {
            //listes de groupes : /groupes
            dispatcher = request.getServletContext().getNamedDispatcher("ControllerGroupes");
            if (dispatcher != null) {
                dispatcher.include(request, response);
            }
        } else if (uriSplit.length == 3) {
            //Detail d'un groupe : /groupes/<id = split[2]>
            // controller détail groupe
            dispatcher = request.getServletContext().getNamedDispatcher("ControllerGroupe");
            if (dispatcher != null) {
                dispatcher.include(request, response);
            }
        } else if (uriSplit[3].equals("billets")) {
            uriBillets(uriSplit, request, response);
        } else {
            //error
        }
    }

    private void uriBillets(String[] uriSplit, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;

        if (uriSplit.length == 4) {
            //listes des billets : /groupes/<id = split[2]>/billets
            // controller des billets
            dispatcher = request.getServletContext().getNamedDispatcher("ControllerBillets");
            if (dispatcher != null) {
                dispatcher.include(request, response);
            }
        } else if (uriSplit.length == 5) {
            //Detail d'un groupe : /groupes/<id = split[2]>/billets/<id = split[4]>
            // controller détail billet
            dispatcher = request.getServletContext().getNamedDispatcher("ControllerBillet");
            if (dispatcher != null) {
                dispatcher.include(request, response);
            }
        } else if (uriSplit[5].equals("commentaires")) {
            uriCommentaires(uriSplit, request, response);
        } else {
            //error
        }

    }

    private void uriCommentaires(String[] uriSplit, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;
        if (uriSplit.length == 6) {
            //listes des commantaire : /groupes/<id = split[1]>/billets/<id = split[3]>/commentaires
            // controller des commentaires
            dispatcher = request.getServletContext().getNamedDispatcher("ControllerComments");
            if (dispatcher != null) {
                dispatcher.include(request, response);
            }
        } else if (uriSplit.length == 7) {
            //Detail d'un groupe : /groupes/<id = split[1]>/billets/<id = split[3]>/commentaires/<id = split[5]>
            // controller détail d'un commentaire
            dispatcher = request.getServletContext().getNamedDispatcher("ControllerComment");
            if (dispatcher != null) {
                dispatcher.include(request, response);
            }
        } else {
            //error    
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        dispatch(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        dispatch(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        dispatch(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        dispatch(request, response);
    }
}
