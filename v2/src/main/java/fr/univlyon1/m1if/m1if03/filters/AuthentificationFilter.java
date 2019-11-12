/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 *
 * @author vasli
 */
@WebFilter("/AuthentificationFilter")
public class AuthentificationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String token = null;
        if (request.getHeader("Authorization") == null) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("Authorization")) {
                        token = cookie.getValue();
                    }
                }
            }
        } else {
            token = request.getHeader("Authorization");
        }

        if (token == null) { // Pas de token on redirige vers la page d'acceuil
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // Test du token
        if (decodeToken(token, request)) {
            chain.doFilter(request, response);
        } else {
            // Token invalide ou expiré
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * Vérifie, décode et ajoute au context le pseudo contenu dans le token
     *
     * @param token Token à verifier et décoder
     * @param request la requête
     * @return true si le token est valide sinon false
     */
    private boolean decodeToken(String token, HttpServletRequest request) {
        try {
            String secret = "fr.univlyon1.m1if.m1if03";
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("p1914433p1917930").build();
            DecodedJWT jwt = verifier.verify(token);
            String uri = jwt.getSubject();
            request.getServletContext().setAttribute("pseudo", uri.split("/")[2]); // Ajoute au context le pseudo
            return true;
        } catch (TokenExpiredException e) {
            System.out.println("Time token expire");
            return false;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

}
