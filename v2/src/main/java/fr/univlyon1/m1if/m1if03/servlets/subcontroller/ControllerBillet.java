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

import fr.univlyon1.m1if.m1if03.mapping.BilletMapper;
import fr.univlyon1.m1if.m1if03.mapping.ContentBilletMapper;
import fr.univlyon1.m1if.m1if03.servlets.IDPos;
import javax.servlet.ServletContext;

@WebServlet(name = "ControllerBillet", urlPatterns = {"/ControllerBillet"})
public class ControllerBillet extends HttpServlet {

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
        GestionBillets gBillet = groupe.getBillets();
        Billet billet = gBillet.getBillet(new Integer(uriSplit[4]));
        if (billet == null) {
            context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.getServletContext().setAttribute("view", "billet");
        request.getServletContext().setAttribute("billet", billet);
        request.getServletContext().setAttribute("data",
                new ObjectMapper().writeValueAsString(new BilletMapper(
                        billet,
                        new Integer(uriSplit[4]),
                        groupe.getNom())));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        context.setAttribute("status", HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();

        String[] uriSplit = ((String) context.getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) context.getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        GestionBillets gBillet = groupe.getBillets();
        Billet billet = gBillet.getBillet(new Integer(uriSplit[IDPos.BILLET_ID_POS]));
        if (billet == null) {
            context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (request.getContentType() != null && request.getContentType().equals("application/json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ContentBilletMapper contentJson;
            try {
                contentJson = objectMapper.readValue(request.getInputStream(), ContentBilletMapper.class);
            } catch (IOException e) {
                context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            billet.setAuteur(contentJson.auteur);
            billet.setContenu(contentJson.contenu);
            billet.setTitre(contentJson.titre);
            context.setAttribute("Location", "/groupes/" + groupe.getNom() + "/billets/" + uriSplit[IDPos.BILLET_ID_POS]);
            context.setAttribute("status", HttpServletResponse.SC_NO_CONTENT);
        } else {
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();

        String[] uriSplit = ((String) context.getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) context.getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        GestionBillets gBillet = groupe.getBillets();
        int id = new Integer(uriSplit[4]);
        Billet billet = gBillet.getBillet(id);
        if (billet != null) {
            gBillet.deleteBillet(id);
            context.setAttribute("status", HttpServletResponse.SC_NO_CONTENT);
        } else {
            context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
