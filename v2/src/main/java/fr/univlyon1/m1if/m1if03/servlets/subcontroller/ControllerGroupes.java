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
import java.io.PrintWriter;

/**
 *
 * @author vasli
 */
@WebServlet(name = "ControllerGroupes", urlPatterns = { "/ControllerGroupes" })
public class ControllerGroupes extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recupération du modèle
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        // On charge les listes de les groupes contenue dans le modèles
        request.getServletContext().setAttribute("groupesList", modele.keySet());

        // ON charge la vue souhaité
        if (request.getContentType() == null) {
            request.getRequestDispatcher("WEB-INF/jsp/groupes.jsp").forward(request, response);
        }
        switch (request.getContentType()) {
        case "text/html":
            request.getRequestDispatcher("WEB-INF/jsp/groupes.jsp").forward(request, response);
            break;
        case "application/json":
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonGroupes = objectMapper.writeValueAsString(createGroupesTab(modele.keySet()));
            out.print(jsonGroupes);
            out.flush();
            break;
        default:
            request.getRequestDispatcher("WEB-INF/jsp/groupes.jsp").forward(request, response);
            break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recupération du modèle
        String author = null, title = null, content = null;

        if (request.getContentType() != null && request.getContentType().equals("application/json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ContentGroupeMapper contentJson = null;
            try {
                contentJson = objectMapper.readValue(request.getInputStream(), ContentGroupeMapper.class);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
        System.out.println(title + " " + content + " " + author + " ");

        if (author == null || author.equals("") || title == null || title.equals("") || content == null
                || content.equals("")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        if (modele.containsKey(title) == false) {
            Groupe g = new Groupe(title, content, author);
            modele.put(title, g);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.addHeader("Location", "/groupes/" + title);

        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
