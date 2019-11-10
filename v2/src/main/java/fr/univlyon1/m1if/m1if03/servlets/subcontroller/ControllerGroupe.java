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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.univlyon1.m1if.m1if03.classes.*;
import fr.univlyon1.m1if.m1if03.mapping.ContentGroupeMapper;
import fr.univlyon1.m1if.m1if03.mapping.GroupeMapper;
import java.io.PrintWriter;


@WebServlet(name = "ControllerGroupe", urlPatterns = { "/ControllerGroupe" })
public class ControllerGroupe extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = ((String) request.getServletContext().getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.getServletContext().setAttribute("groupe", groupe);
        // ON charge la vue souhaité
        if (request.getContentType() == null) {
            request.getRequestDispatcher("WEB-INF/jsp/groupe.jsp").forward(request, response);
        }
        switch (request.getContentType()) {
        case "text/html":
            request.getRequestDispatcher("WEB-INF/jsp/groupe.jsp").forward(request, response);
            break;
        case "application/json":
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonGroupes = objectMapper.writeValueAsString(new GroupeMapper(groupe));
            out.print(jsonGroupes);
            out.flush();
            break;
        default:
            request.getRequestDispatcher("WEB-INF/jsp/groupe.jsp").forward(request, response);
            break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = ((String) request.getServletContext().getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);

        if (groupe == null) { // On créé un nouveau groupe
            if (request.getContentType() != null && request.getContentType().equals("application/json")) {
                ObjectMapper objectMapper = new ObjectMapper();
                ContentGroupeMapper contentJson = null;
                try {
                    contentJson = objectMapper.readValue(request.getInputStream(), ContentGroupeMapper.class);
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                // On vérifie que les données nécessaire à la création d'un groupe sont bien
                // fournie
                if (contentJson == null || contentJson.proprietaire == null || contentJson.proprietaire.equals("")
                        || contentJson.nom == null || contentJson.nom.equals("") || contentJson.description == null
                        || contentJson.description.equals("")) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                Groupe g = new Groupe(contentJson.nom, contentJson.description, contentJson.proprietaire);
                g.setMembres(contentJson.membres);
                modele.put(contentJson.nom, g);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.addHeader("Location", "/groupes/" + contentJson.nom);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else { // On met à jour un groupe
            if (request.getContentType() != null && request.getContentType().equals("application/json")) {
                ObjectMapper objectMapper = new ObjectMapper();
                ContentGroupeMapper cMapper = objectMapper.readValue(request.getInputStream(),
                        ContentGroupeMapper.class);
                if (cMapper != null) {
                    groupe.setAuteur(cMapper.proprietaire);
                    groupe.setDescription(cMapper.description);
                    groupe.setNom(cMapper.nom);
                    groupe.setMembres(cMapper.membres);
                    response.addHeader("Location", "/groupes/" + groupe.getNom());
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = ((String) request.getServletContext().getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        if (modele.containsKey(uriSplit[2])) {
            modele.remove(uriSplit[2]);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            response.addHeader("Location", "/groupes");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

}
