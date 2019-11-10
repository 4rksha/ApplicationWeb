<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Commentaire" %>
<jsp:useBean scope="application" id="path" class="java.lang.String"/>
<jsp:useBean scope="application" id="billet" class="fr.univlyon1.m1if.m1if03.classes.Billet"/>
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
        <title>Commentaires</title>
    </head>

    <header>
        <nav class="navbar fixed-top navbar-expand-lg navbar-dark bg-dark">
            <span class="navbar-brand">Commentaires</span>
        </nav>
    </header>
    <div class="container">
        <br/>
        <br/>
        <br/>
        <h1 class="text-left">Liste des commentaires</h1>
        <br/>
        <div class="list-group">
            <%
            for (int i = 0; i < billet.getCommentaires().size(); ++i) {%>
             <div class="card" style="max-width: 600px;">
                <div class="card-body">
                    
                    <p class="card-text"><a href="<%= path + i %>">Commentaire <%= i %></p>
                   
                </div>
                
            </div>
            <%}%>
        </div>

</body>
</html>
