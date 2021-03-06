/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.subcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Groupe;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.univlyon1.m1if.m1if03.mapping.ContentGroupeMapper;
import fr.univlyon1.m1if.m1if03.mapping.GroupeMapper;
import javax.servlet.ServletContext;

@WebServlet(name = "ControllerGroupe", urlPatterns = {"/ControllerGroupe"})
public class ControllerGroupe extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();

        String[] uriSplit = ((String) context.getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) context.getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.getServletContext().setAttribute("groupe", groupe);
        request.getServletContext().setAttribute("view", "groupe");
        request.getServletContext().setAttribute("data",
                new ObjectMapper().writeValueAsString(new GroupeMapper(groupe)));

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
        if (groupe == null) { // On créé un nouveau groupe
            if (request.getContentType() != null && request.getContentType().equals("application/json")) {
                ObjectMapper objectMapper = new ObjectMapper();
                ContentGroupeMapper contentJson;
                try {
                    contentJson = objectMapper.readValue(request.getInputStream(), ContentGroupeMapper.class);
                } catch (IOException e) {
                    context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                // On vérifie que les données nécessaire à la création d'un groupe sont bien
                // fournie
                if (contentJson == null || contentJson.proprietaire == null || contentJson.proprietaire.equals("")
                        || contentJson.nom == null || contentJson.nom.equals("") || contentJson.description == null
                        || contentJson.description.equals("")) {
                    context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                Groupe g = new Groupe(contentJson.nom, contentJson.description, contentJson.proprietaire);
                g.setMembres(contentJson.membres);
                modele.put(contentJson.nom, g);
                context.setAttribute("status", HttpServletResponse.SC_NO_CONTENT);
                context.setAttribute("Location", "/groupes/" + contentJson.nom);
            } else {
                context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
            }
        } else { // On met à jour un groupe
            if (request.getContentType() != null && request.getContentType().equals("application/json")) {
                modele.remove(uriSplit[2]);
                ObjectMapper objectMapper = new ObjectMapper();
                ContentGroupeMapper cMapper = objectMapper.readValue(request.getInputStream(),
                        ContentGroupeMapper.class);
                if (cMapper != null) {
                    groupe.setAuteur(cMapper.proprietaire);
                    groupe.setDescription(cMapper.description);
                    groupe.setNom(cMapper.nom);
                    groupe.setMembres(cMapper.membres);
                    modele.put(groupe.getNom(), groupe);
                    context.setAttribute("Location", "/groupes/" + groupe.getNom());
                    context.setAttribute("status", HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
            }
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        String[] uriSplit = ((String) context.getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) context.getAttribute("groupes");
        if (modele.containsKey(uriSplit[2])) {
            modele.remove(uriSplit[2]);
            context.setAttribute("status", HttpServletResponse.SC_NO_CONTENT);
            context.setAttribute("location", "/groupes");
        } else {
            context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
        }

    }

}
