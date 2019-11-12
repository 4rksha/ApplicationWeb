/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.subcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Billet;
import fr.univlyon1.m1if.m1if03.classes.Commentaire;
import fr.univlyon1.m1if.m1if03.classes.GestionBillets;
import fr.univlyon1.m1if.m1if03.classes.Groupe;
import fr.univlyon1.m1if.m1if03.mapping.CommentMapper;
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

@WebServlet(name = "ControllerComment", urlPatterns = {"/ControllerComment"})
public class ControllerComment extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        String[] uriSplit = ((String) context.getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) context.getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[IDPos.GROUPE_ID_POS]);
        if (groupe == null) {
            context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        GestionBillets gBillet = groupe.getBillets();
        Billet billet = gBillet.getBillet(Integer.valueOf(uriSplit[IDPos.BILLET_ID_POS]));
        if (billet == null) {
            //error
            context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
        } else {
            Commentaire c = billet.getCommentaire(Integer.valueOf(uriSplit[IDPos.COMMENTAIRE_ID_POS]));
            if (c == null) {
                context.setAttribute("status", HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.getServletContext().setAttribute("view", "commentaire");
            request.getServletContext().setAttribute("commentaire", c);
            request.getServletContext().setAttribute("data",
                    new ObjectMapper().writeValueAsString(new CommentMapper(
                            uriSplit[IDPos.COMMENTAIRE_ID_POS], c)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        String[] uriSplit = ((String) context.getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) context.getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[IDPos.GROUPE_ID_POS]);
        if (groupe == null) {
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        GestionBillets gBillet = groupe.getBillets();
        Billet billet = gBillet.getBillet(Integer.valueOf(uriSplit[IDPos.BILLET_ID_POS]));
        if (billet == null) {
            //error
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Commentaire c = billet.getCommentaires().get(Integer.valueOf(uriSplit[IDPos.COMMENTAIRE_ID_POS]));
        if (c != null) {
            if (request.getContentType() != null && request.getContentType().equals("application/json")) {
                ObjectMapper om = new ObjectMapper();
                ContentCommentMapper contentJson;

                try {
                    contentJson = om.readValue(request.getInputStream(), ContentCommentMapper.class);
                } catch (IOException e) {
                    context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                // TODO bon format
                String text = contentJson.texte;
                // TODO bon format
                String auteur = contentJson.auteur;
                if (!text.isEmpty() && !auteur.isEmpty()) {
                    c.setCommentaire(auteur, text);
                    context.setAttribute("status", HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
            }
        }
        context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        String[] uriSplit = ((String) context.getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) context.getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[IDPos.GROUPE_ID_POS]);
        if (groupe == null) {
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        GestionBillets gBillet = groupe.getBillets();
        Billet billet = gBillet.getBillet(Integer.valueOf(uriSplit[IDPos.BILLET_ID_POS]));
        if (billet == null) {
            //error
            context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
        } else {
            Commentaire c = billet.getCommentaires().get(Integer.valueOf(uriSplit[IDPos.COMMENTAIRE_ID_POS]));
            if (c == null) {
                context.setAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
            } else {
                billet.removeCommentaire(Integer.valueOf(uriSplit[IDPos.COMMENTAIRE_ID_POS]));
        context.setAttribute("status", HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }

}
