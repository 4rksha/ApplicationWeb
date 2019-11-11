/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.servlets.subcontroller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.univlyon1.m1if.m1if03.classes.Pseudo;

@WebServlet(name = "ControllerUser", urlPatterns = { "/ControllerUser/*" })
public class ControllerUser extends HttpServlet {

    /**
     * Initialisation de la liste des users
     * 
     * @param sc
     */
    @Override
    public void init(ServletConfig sc) {
        sc.getServletContext().setAttribute("users", new ArrayList<String>());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> users = (ArrayList<String>) request.getServletContext().getAttribute("users");
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonGroupes = objectMapper.writeValueAsString(createTabUser(users));
        out.print(jsonGroupes);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // String[] uriSplit = ((String)
        // request.getServletContext().getAttribute("URI")).split("/");
        String[] uriSplit = request.getRequestURI().split("/");
        List<String> users = (ArrayList<String>) request.getServletContext().getAttribute("users");

        switch (uriSplit[2]) {
        case "login": // Connexion
            String pseudo = null;
            if (request.getContentType() != null && request.getContentType().equals("application/json")) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Pseudo jsonResult = objectMapper.readValue(request.getInputStream(), Pseudo.class);

                    if (jsonResult != null && jsonResult.pseudo.equals("") == false) {
                        pseudo = jsonResult.pseudo;
                        response.setStatus(HttpServletResponse.SC_CREATED);
                        String token = createJWT("/users/" + pseudo, 60000);// Temps de validité 10 minutes
                        response.setHeader("Authorization", token);
                        Cookie cookie = new Cookie("Authorization", token);
                        cookie.setPath("/");
                        // cookie.setSecure(true);
                        response.addCookie(cookie);
                        users.add(pseudo);
                    }
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                pseudo = request.getParameter("pseudo");
                if (pseudo != null && pseudo.equals("") == false) {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    String token = createJWT("/users/" + pseudo, 600000);// Temps de validité 10 minutes
                    response.setHeader("Authorization", token);
                    response.addCookie(new Cookie("Authorization", token));
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
            break;
        case "logout":
            response.setHeader("Authorization", null);
            Cookie cookie = new Cookie("Authorization", null);
            cookie.setPath("/");
            // cookie.setSecure(true);
            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            break;
        default:
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            break;
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

    private String[] createTabUser(List<String> users) {
        String[] tab = new String[users.size()];
        int i = 0;
        for (String user : users) {
            tab[i] = "/users/" + user;
            i++;
        }
        return tab;
    }

    /**
     * Génère un token
     * 
     * @param uriUser URI de l'user qui demande un token
     * @param ms      temps en ms de validité d'un token
     * @return token
     */
    private String createJWT(String uriUser, long ms) {

        String secret = "fr.univlyon1.m1if.m1if03";
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Date now = new Date(System.currentTimeMillis());
        long expDataMS = now.getTime() + ms;
        Date expData = new Date(expDataMS);
        String issuer = "p1914433p1917930";
        String token = JWT.create().withIssuer(issuer).withSubject(uriUser).withExpiresAt(expData).sign(algorithm);
        return token;
    }
}
