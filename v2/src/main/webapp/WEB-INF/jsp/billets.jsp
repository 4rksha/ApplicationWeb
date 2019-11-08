<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.GestionBillets" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.*" %>
<jsp:useBean scope="application" id="Gbillets" class="fr.univlyon1.m1if.m1if03.classes.GestionBillets"/>

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
    <title>Gestionnaire de billet</title>
</head>
<body>
    <header>
        <nav class="navbar fixed-top navbar-expand-lg navbar-dark bg-dark">
            <span class="navbar-brand">Gestionnaire de billet</span>
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
        <% for(int i = 0; i < Gbillets.getNbBillets(); ++i) { 
                billet = Gbillets.getBillet(i); 
        %>
            <div class="list-group-item">
              <div class="d-flex w-100 justify-content-between">
                  <h5 class="mb-1">id <%= i %> : <%= billet.getTitre()%></h5>
              </div>
              <small>Auteur : <%= billet.getAuteur() %></small>
            </div>
        <%}%>
    </div>

</body>
</html>
