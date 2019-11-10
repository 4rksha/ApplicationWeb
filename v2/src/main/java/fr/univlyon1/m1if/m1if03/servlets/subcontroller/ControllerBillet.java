/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.subcontroller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.univlyon1.m1if.m1if03.classes.*;
import fr.univlyon1.m1if.m1if03.mapping.BilletMapper;
import fr.univlyon1.m1if.m1if03.mapping.ContentBilletMapper;


@WebServlet(name = "ControllerBillet", urlPatterns = { "/ControllerBillet" })
public class ControllerBillet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = ((String) request.getServletContext().getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        GestionBillets gBillet = groupe.getBillets();
        Billet billet = gBillet.getBillet(new Integer(uriSplit[4]));
        if (billet == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        // On regarde s'il y a eu une modification depuis la dernière demande de billet
        long timeRequest = request.getDateHeader("If-Modified-Since");
        if (timeRequest != -1 && (timeRequest / 1000) >= (billet.getLastModifTime() / 1000)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        // On ajoute la date de la derniere modification dans le header
        response.addDateHeader("Last-Modified", billet.getLastModifTime());
        request.getServletContext().setAttribute("billet", billet);
        // ON charge la vue souhaité
        if (request.getContentType() == null) {
            request.getRequestDispatcher("WEB-INF/jsp/billet.jsp").forward(request, response);
        }
        // On selctionne la vue en fonction du content type
        switch (request.getContentType()) {
        case "text/html":
            request.getRequestDispatcher("WEB-INF/jsp/billet.jsp").forward(request, response);
            break;
        case "application/json":
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonGroupes = objectMapper
                    .writeValueAsString(new BilletMapper(billet, new Integer(uriSplit[4]), groupe.getNom()));
            out.print(jsonGroupes);
            out.flush();
            break;
        default:
            request.getRequestDispatcher("WEB-INF/jsp/billet.jsp").forward(request, response);
            break;
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
        String[] uriSplit = ((String) request.getServletContext().getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        GestionBillets gBillet = groupe.getBillets();
        Billet billet = gBillet.getBillet(new Integer(uriSplit[4]));
        if (billet == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (request.getContentType() != null && request.getContentType().equals("application/json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ContentBilletMapper contentJson = null;
            try {
                contentJson = objectMapper.readValue(request.getInputStream(), ContentBilletMapper.class);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            billet.setAuteur(contentJson.auteur);
            billet.setContenu(contentJson.contenu);
            billet.setTitre(contentJson.titre);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = ((String) request.getServletContext().getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[2]);
        if (groupe == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        GestionBillets gBillet = groupe.getBillets();
        int id = new Integer(uriSplit[4]);
        Billet billet = gBillet.getBillet(id);
        if (billet != null) {
            gBillet.deleteBillet(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
