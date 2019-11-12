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

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Billet;
import fr.univlyon1.m1if.m1if03.classes.GestionBillets;
import fr.univlyon1.m1if.m1if03.classes.Groupe;
import fr.univlyon1.m1if.m1if03.mapping.ContentBilletMapper;
import javax.servlet.ServletContext;

/**
 * Sous controlleur qui gère les billets et leurs créations
 */
@WebServlet(name = "ControllerBillets", urlPatterns = {"/ControllerBillets"})
public class ControllerBillets extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();

        String[] uriSplit = ((String) context.getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) context.getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        GestionBillets gBillets = groupe.getBillets();
        request.getServletContext().setAttribute("Gbillets", gBillets);
        request.getServletContext().setAttribute("view", "billets");

        request.getServletContext().setAttribute("data",
                new ObjectMapper().writeValueAsString(createBilletTab(gBillets, groupe.getNom())));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();

        String[] uriSplit = ((String) request.getServletContext().getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        GestionBillets gBillets = groupe.getBillets();
        if (request.getContentType() != null && request.getContentType().equals("application/json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ContentBilletMapper contentJson;
            try {
                contentJson = objectMapper.readValue(request.getInputStream(), ContentBilletMapper.class);
            } catch (IOException e) {
                context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if (contentJson != null && contentJson.auteur != null && contentJson.contenu != null
                    && contentJson.titre != null) {
                gBillets.add(new Billet(contentJson.titre, contentJson.contenu, contentJson.auteur));
                context.setAttribute("status", HttpServletResponse.SC_CREATED);
                context.setAttribute("Location", "/groupes/" + groupe.getNom() + "/billets/" + (gBillets.getNbBillets() - 1));

            } else {
                context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            if (request.getParameter("titre") != null && request.getParameter("contenu") != null
                    && request.getParameter("auteur") != null) {
                gBillets.add(new Billet(request.getParameter("titre"), request.getParameter("contenu"),
                        request.getParameter("auteur")));
                context.setAttribute("status", HttpServletResponse.SC_CREATED);
                context.setAttribute("Location", "/groupes/" + groupe.getNom() + "/billets/" + (gBillets.getNbBillets() - 1));

            } else {
                context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        context.setAttribute("status", HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        context.setAttribute("status", HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    /**
     * Créé un tableau d'url de chaque billet
     *
     * @param gBillet l'ensemble des billets
     * @param groupe nom du groupe où se trouve le billet
     * @return tableau d'uri
     */
    public String[] createBilletTab(GestionBillets gBillet, String groupe) {
        int size = gBillet.getNbBillets();
        String[] billets = new String[size];
        for (int i = 0; i < size; ++i) {
            billets[i] = "/groupes/" + groupe + "/billets/" + i;
        }
        return billets;
    }
}
