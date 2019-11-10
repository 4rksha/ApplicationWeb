/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.subcontroller;

import fr.univlyon1.m1if.m1if03.classes.Billet;
import fr.univlyon1.m1if.m1if03.classes.Commentaire;
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
@WebServlet(name = "ControllerComments", urlPatterns = {"/ControllerComments"})
public class ControllerComments extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        String uri = (String) request.getServletContext().getAttribute("URI");
        String[] uriSplit = uri.split("/");
        Groupe gr = modele.get(uriSplit[IDPos.GROUPE_ID_POS]);
        if (gr == null) {
            //error
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Billet billet = gr.getgBillets().getBillet(uriSplit[IDPos.BILLET_ID_POS]);
        if (billet == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        request.getServletContext().setAttribute("billet", billet);
        String path = uri.split("\\?")[0];
        request.getServletContext().setAttribute("path", path);
        request.getRequestDispatcher("WEB-INF/jsp/commentaires.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] uriSplit = ((String) request.getServletContext().getAttribute("URI")).split("/");
        Map<String, Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        Groupe gr = modele.get(uriSplit[IDPos.GROUPE_ID_POS]);
        if (gr == null) {
            //error
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Billet billet = gr.getgBillets().getBillet(uriSplit[IDPos.BILLET_ID_POS]);
        if (billet == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String text = (String) request.getParameter("text");
        String auteur = (String) request.getParameter("auteur");
        if (text == null || text.isEmpty() || auteur == null || auteur.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            billet.addCommentaire(auteur, text);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
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
