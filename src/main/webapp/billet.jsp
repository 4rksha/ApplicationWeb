<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.GestionBillets" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.*" %>
<jsp:useBean scope="application" id="groupes" type="java.util.Map"/>
<%! private GestionBillets gBillet;%>

<% 
    if (!request.isRequestedSessionIdValid()) {
        response.sendRedirect("index.html");
    } else {
        // Récupération du groupe
        String nameGroupe = (String) session.getAttribute("groupe");
        if (groupes.containsKey(nameGroupe)) {
            Groupe g = (Groupe) groupes.get(nameGroupe);
            gBillet = g.getgBillets();
        } else {
            String pseudo = (String) session.getAttribute("pseudo");
            List<String> liste = new ArrayList<String>();
            liste.add(pseudo);
            gBillet = new GestionBillets();
            Groupe g = new Groupe(
                    nameGroupe,
                    "",
                    pseudo,
                    liste,
                    gBillet
            );
            groupes.put(nameGroupe, g);
        }
        
        // Traitement de la requête
        if (request.getMethod().equals("POST")) {
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
    }
%>
<!doctype html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <link rel="stylesheet" 
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" 
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" 
          crossorigin="anonymous">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" 
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" 
            crossorigin="anonymous">
    </script>
    <meta http-equiv="refresh" content="5;url=billet.jsp" />
    <title>Gestionnaire de billet</title>
</head>
<body>
    <header>
        <nav class="navbar fixed-top navbar-expand-lg navbar-dark bg-dark">
            <span class="navbar-brand">Gestionnaire de billet</span>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
              <ul class="navbar-nav mr-auto">
                <li class="nav-item acitve">
                    <span class="navbar-brand"><%= session.getAttribute("pseudo")%> : <%= session.getAttribute("groupe")%></span>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Groupes
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <%! String[] groups; %>
                        <%
                            System.out.println(groups);
                            groups =(String[]) groupes.keySet().toArray(new String[groupes.keySet().size()]);
                            for (int i = 0; i < groups.length; ++i) {%>
                            <a class="dropdown-item" href="Init?group=<%=groups[i]%>"><%=groups[i]%></a>
                        <%}%>
                    </div>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="saisie.html">Nouveau billet</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="Deco">Se déconnecter</a> 
                </li>
              </ul>
            </div>
        </nav>
    </header>
    <div class="container">
        <br/>
        <br/>
        <br/>
        <h1 class="text-left">Liste des billets</h1>
        <br/>
        <div class="list-group">
        <%! Billet billet;%>
        <% for(int i = 0; i < gBillet.getNbBillets(); ++i) { 
                billet = gBillet.getBillet(i); 
        %>
            <a href="detailBillet.jsp?id=<%=i%>" class="list-group-item list-group-item-action">
              <div class="d-flex w-100 justify-content-between">
                <h5 class="mb-1"><%= billet.getTitre()%></h5>
              </div>
              <small>Auteur : <%= billet.getAuteur() %></small>
            </a>
        <%}%>
    </div>

</body>
</html>
