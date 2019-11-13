/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.subcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.univlyon1.m1if.m1if03.classes.Groupe;
import fr.univlyon1.m1if.m1if03.mapping.ContentGroupeMapper;
import javax.servlet.ServletContext;

@WebServlet(name = "ControllerGroupes", urlPatterns = {"/ControllerGroupes"})
public class ControllerGroupes extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        // Recupération du modèle
        Map<String, Groupe> modele = (HashMap<String, Groupe>) context.getAttribute("groupes");
        // On charge les listes de les groupes contenue dans le modèles
        request.getServletContext().setAttribute("groupesList", modele.keySet());
        request.getServletContext().setAttribute("view", "groupes");
        request.getServletContext().setAttribute("data",
                new ObjectMapper().writeValueAsString(createGroupesTab(modele.keySet())));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        // Recupération du modèle
        String author = null, title = null, content = null;

        if (request.getContentType() != null && request.getContentType().equals("application/json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ContentGroupeMapper contentJson;
            try {
                contentJson = objectMapper.readValue(request.getInputStream(), ContentGroupeMapper.class);
            } catch (IOException e) {
                context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if (contentJson != null) {
                author = contentJson.proprietaire;
                title = contentJson.nom;
                content = contentJson.description;
            }
        } else {
            author = request.getParameter("proprietaire");
            title = request.getParameter("nom");
            content = request.getParameter("description");
        }
        if (author == null || author.equals("") || title == null || title.equals("") || content == null
                || content.equals("")) {
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        if (modele.containsKey(title) == false) {
            Groupe g = new Groupe(title, content, author);
            modele.put(title, g);

            context.setAttribute("status", HttpServletResponse.SC_CREATED);
            response.addHeader("Location", "/groupes/" + title);

        } else {
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Créé un tableau d'url à partir d'un set de groupes
     *
     * @param namesGroupe
     * @return tableau d'uri
     */
    private String[] createGroupesTab(Set<String> namesGroupe) {
        String[] groupes = new String[namesGroupe.size()];
        int i = 0;
        for (String groupe : namesGroupe) {
            groupes[i] = "/groupes/" + groupe;
            i++;
        }
        return groupes;
    }

}
