/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.old;

import fr.univlyon1.m1if.m1if03.classes.Billet;
import fr.univlyon1.m1if.m1if03.classes.GestionBillets;
import fr.univlyon1.m1if.m1if03.classes.Groupe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author vasli
 */
@WebServlet(name = "Controleur", urlPatterns = {"/billets","/billet"})
public class Controleur extends HttpServlet {
    
    @Override
    public void init( ServletConfig sc) {
        sc.getServletContext().setAttribute("groupes", new HashMap<String, Groupe>());
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Map<String,Groupe> modele = (HashMap<String, Groupe>) request.getServletContext().getAttribute("groupes");
        String chemin = request.getRequestURI();
        String nameGroupe = (String) session.getAttribute("groupe");
        GestionBillets gBillet;
        
        if (modele.containsKey(nameGroupe)) {
            Groupe g = (Groupe) modele.get(nameGroupe);
            gBillet = g.getBillets();
        } else {
            String pseudo = (String) session.getAttribute("pseudo");
            gBillet = new GestionBillets();
            Groupe g = new Groupe(
                    nameGroupe,
                    "",
                    pseudo
            );
            modele.put(nameGroupe, g);
        }
        
        // Traitement de la requête
        if (request.getMethod().equals("POST") && chemin.equals("/billets")) {
            if (request.getParameter("contenu") != null){
                gBillet.add(
                    new Billet(
                        request.getParameter("titre"),
                        request.getParameter("contenu"),
                        (String) session.getAttribute("pseudo")
                    )
                );
            }
        }
        if (request.getMethod().equals("POST")) {
            if (request.getParameter("commentaire") != null 
                    && request.getParameter("id") != null) {
                Integer id = new Integer(request.getParameter("id"));
                gBillet.addCommantaireBillet(
                        (String) session.getAttribute("pseudo"),
                        id, 
                        request.getParameter("commentaire"));
            }
        }
        
        
        if (chemin.equals("/billets")) {
            request.getServletContext().setAttribute("Gbillets", gBillet);
            System.out.println(modele.keySet());
            request.getServletContext().setAttribute(
                    "setGroup", 
                    modele.keySet()
            );
            request.getRequestDispatcher("WEB-INF/jsp/billets.jsp").forward(request, response);
        } else if (chemin.equals("/billet")) {
            Billet billet = gBillet.getBillet(new Integer(request.getParameter("id")));
            if (billet == null) {
                request.getServletContext().setAttribute("Gbillets",gBillet); 
                request.getRequestDispatcher("WEB-INF/jsp/billets.jsp").forward(request, response);
            } else {
                long timeRequest = request.getDateHeader("If-Modified-Since");
//                System.out.println(timeRequest+ " " +billet.getLastModifTime()/1000);
//                System.out.println(timeRequest/1000 >= (billet.getLastModifTime()/1000));
                if (timeRequest != -1 
                        && (timeRequest / 1000) >= (billet.getLastModifTime() / 1000)
                ) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                } else {
                    response.addDateHeader("Last-Modified", billet.getLastModifTime());
                    request.getServletContext().setAttribute("billet",billet);              
                    request.getRequestDispatcher("WEB-INF/jsp/billet.jsp").forward(request, response);
                }
                
            }
        }       
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response); 
    }

}
