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
@WebServlet(name = "ControllerBillet", urlPatterns = {"/ControllerBillet"})
public class ControllerBillet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

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
        GestionBillets gBillet = groupe.getgBillets();
        Billet billet = gBillet.getBillet(new Integer(uriSplit[4]));
        if (billet == null) {
            //error
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            long timeRequest = request.getDateHeader("If-Modified-Since");
            if (timeRequest != -1 
                    && (timeRequest / 1000) >= (billet.getLastModifTime() / 1000)
            ) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            } else {
                response.addDateHeader("Last-Modified", billet.getLastModifTime());
                request.getServletContext().setAttribute("billet",billet);              
                request.getRequestDispatcher("WEB-INF/jsp/billet.jsp").forward(request, response);
            }
            
        }
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = request.getRequestURI().split("/");
        Map<String,Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            //error
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        GestionBillets gBillet = groupe.getgBillets();
        Billet billet = gBillet.getBillet(new Integer(uriSplit[4]));
        if (billet == null) {
            //error
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            if (request.getParameter("author") != null) {
                billet.setAuteur(request.getParameter("author"));
            } 
            if (request.getParameter("content") != null) {
                billet.setContenu(request.getParameter("content"));
            } 
            if (request.getParameter("title") != null) {
                billet.setTitre(request.getParameter("title"));
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = request.getRequestURI().split("/");
        Map<String,Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        GestionBillets gBillet = groupe.getgBillets();
        int id = new Integer(uriSplit[4]);
        Billet billet = gBillet.getBillet(id);
        if (billet != null) {
            gBillet.deleteBillet(id); 
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


}
