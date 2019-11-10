/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.subcontroller;

import fr.univlyon1.m1if.m1if03.classes.Billet;
import fr.univlyon1.m1if.m1if03.classes.Commentaire;
import fr.univlyon1.m1if.m1if03.classes.GestionBillets;
import fr.univlyon1.m1if.m1if03.classes.Groupe;
import fr.univlyon1.m1if.m1if03.servlets.IDPos;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author vasli
 */
@WebServlet(name = "ControllerComment", urlPatterns = {"/ControllerComment"})
public class ControllerComment extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = ((String) request.getServletContext().getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[IDPos.GROUPE_ID_POS]);
        if (groupe == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        GestionBillets gBillet = groupe.getgBillets();
        Billet billet = gBillet.getBillet(Integer.valueOf(uriSplit[IDPos.BILLET_ID_POS]));
        if (billet == null) {
            //error
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            Commentaire c = billet.getCommentaire(Integer.valueOf(uriSplit[IDPos.COMMENTAIRE_ID_POS]));
            if (c == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            request.getServletContext().setAttribute("commentaire", c);
            request.getServletContext().setAttribute("id", uriSplit[IDPos.COMMENTAIRE_ID_POS] + "");

            request.getRequestDispatcher("WEB-INF/jsp/commentaire.jsp").forward(request, response);
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
        String[] uriSplit = ((String) request.getServletContext().getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[IDPos.GROUPE_ID_POS]);
        if (groupe == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        GestionBillets gBillet = groupe.getgBillets();
        Billet billet = gBillet.getBillet(Integer.valueOf(uriSplit[IDPos.BILLET_ID_POS]));
        if (billet == null) {
            //error
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            Commentaire c = billet.getCommentaires().get(Integer.valueOf(uriSplit[IDPos.COMMENTAIRE_ID_POS]));
            // TODO bon format
            String text = "nouveau titre";
            // TODO bon format
            String auteur = "nouvel auteur";
            if (c == null || text == null || text.isEmpty()
                    || auteur == null || auteur.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                c.setCommentaire(auteur, text);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = ((String) request.getServletContext().getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe groupe = modele.get(uriSplit[IDPos.GROUPE_ID_POS]);
        if (groupe == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        GestionBillets gBillet = groupe.getgBillets();
        Billet billet = gBillet.getBillet(Integer.valueOf(uriSplit[IDPos.BILLET_ID_POS]));
        if (billet == null) {
            //error
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            Commentaire c = billet.getCommentaires().get(Integer.valueOf(uriSplit[IDPos.COMMENTAIRE_ID_POS]));
            if (c == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                billet.removeCommentaire(Integer.valueOf(uriSplit[IDPos.COMMENTAIRE_ID_POS]));
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }

}
