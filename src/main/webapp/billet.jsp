<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.GestionBillets" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.*" %>
<%! private GestionBillets gBillet = GestionBillets.getInstance();%>

<% 
    if (!request.isRequestedSessionIdValid()) {
        response.sendRedirect("index.html");
    } else {
        if (request.getMethod().equals("POST")) {
            if (request.getParameter("contenu") != null){
                gBillet.add((String) session.getAttribute("groupe"), 
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
                    <span class="navbar-brand">Compte: <%= session.getAttribute("pseudo")%></span>
                </li>
                <li class="nav-item acitve">
                    <span class="navbar-brand">Groupe: <%= session.getAttribute("groupe")%></span>
                </li>
                <li class="nav-item acitve">
                       <!-- TODO : Rajouter la liste des groupes avec gBillet.getGroupes()-->
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="saisie.html">Saisir un nouveau billet</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="Deco">Se déconnecter</a> 
                </li>
              </ul>
            </div>
        </nav>
    </header>
    <div class="container">
<!--        <h2 class="row">Hello <%= session.getAttribute("pseudo")%> !</h2>
        <div class="row">
            <a class="btn btn-primary" href="saisie.html" role="button">Saisir un nouveau billet</a>
            <a class="btn btn-secondary" href="Deco" role="button">Se déconnecter</a> 
        </div>-->
        <br/>
        <br/>
        <br/>
        <h1 class="text-left">Liste des billets</h1>
        <br/>
        <div class="list-group">
        <%! Billet billet;%>
        <% String groupe = (String) session.getAttribute("groupe");%>
        <% for(int i = 0; i < gBillet.getNbBillets(groupe); ++i) { 
                billet = gBillet.getBillet(groupe, i); 
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
