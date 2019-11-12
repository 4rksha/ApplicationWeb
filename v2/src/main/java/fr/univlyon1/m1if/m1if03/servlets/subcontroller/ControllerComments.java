/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.subcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Billet;
import fr.univlyon1.m1if.m1if03.classes.Groupe;
import fr.univlyon1.m1if.m1if03.mapping.CommentsMapper;
import fr.univlyon1.m1if.m1if03.mapping.ContentCommentMapper;
import fr.univlyon1.m1if.m1if03.servlets.IDPos;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ControllerComments", urlPatterns = {"/ControllerComments"})
public class ControllerComments extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();

        Map<String, Groupe> modele = (HashMap<String, Groupe>) context.getAttribute("groupes");

        String uri = (String) request.getServletContext().getAttribute("URI");
        String[] uriSplit = uri.split("/");
        Groupe gr = modele.get(uriSplit[IDPos.GROUPE_ID_POS]);
        if (gr == null) {
            //error
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Billet billet = gr.getBillets().getBillet(uriSplit[IDPos.BILLET_ID_POS]);
        if (billet == null) {
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // On charge les listes des groupes contenue dans le modèles
        context.setAttribute("billet", billet);
        context.setAttribute("view", "commentaires");
        context.setAttribute("data",
                new ObjectMapper().writeValueAsString(new CommentsMapper(
                        uriSplit[IDPos.BILLET_ID_POS],
                        billet,
                        uriSplit[IDPos.GROUPE_ID_POS]).mapped()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        String[] uriSplit = ((String) context.getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) context.getAttribute("groupes");
        Groupe gr = modele.get(uriSplit[IDPos.GROUPE_ID_POS]);
        if (gr == null) {
            //error
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Billet billet = gr.getBillets().getBillet(uriSplit[IDPos.BILLET_ID_POS]);
        if (billet == null) {
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (request.getContentType() != null && request.getContentType().equals("application/json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ContentCommentMapper contentJson;
            try {
                contentJson = objectMapper.readValue(request.getInputStream(), ContentCommentMapper.class);
            } catch (IOException e) {
                context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if (contentJson != null && contentJson.auteur != null && contentJson.texte != null) {
                billet.addCommentaire(contentJson.auteur, contentJson.texte);
                context.setAttribute("status", HttpServletResponse.SC_CREATED);
                context.setAttribute("Location", "/groupes/" + gr.getNom()
                        + "/billets/" + uriSplit[IDPos.BILLET_ID_POS]
                        + "/commentaires/" + (billet.getCommentaires().size() - 1));
                return;
            }

        }
        context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
}
