<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.GestionBillets" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.*" %>
<%! private GestionBillets gBillet = GestionBillets.getInstance();%>

<% 
    if (session.getAttribute("pseudo") == null) {
        response.sendRedirect("index.html");
    }
    if (request.getMethod().equals("POST")) {
    if (request.getParameter("contenu") != null){
        gBillet.add(new Billet(
                    request.getParameter("titre"),
                    request.getParameter("contenu"),
                    (String) session.getAttribute("pseudo")
                )
        );
    }
    else if (request.getParameter("commentaire") != null 
            && request.getParameter("name") != null) {
        gBillet.addCommantaireBillet(
                (String) session.getAttribute("pseudo"),
                request.getParameter("name"), 
                request.getParameter("commentaire"));
    }
} %>
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
    <title>Billet</title>
</head>
<body>
    <div class="container">
        <h2 class="row">Hello <%= session.getAttribute("pseudo")%> !</h2>
        <div class="row">
            <a class="btn btn-primary" href="saisie.html" role="button">Saisir un nouveau billet</a>
            <a class="btn btn-secondary" href="Deco" role="button">Se déconnecter</a> 
        </div>

        <%! Billet billet;%>
        <% for(int i = 0; i < gBillet.getNbBillets(); ++i) { 
                billet = gBillet.getBillet(i); 
        %>
        <br/>
        <div class="card  text-left" style="max-width: 600px;">
            <div class="card-header">
                <c:out value="<%= billet.getTitre()%>"/>
            </div>
            <div class="card-body">
                <p class="card-text"><%= billet.getContenu()%></small></p>
                <p class="card-text"><small class="text-muted">écrit par <%= billet.getAuteur() %></small></p>
                <ul class="list-group">
                    <% for (int j = 0; j < billet.getCommentaires().size(); j++) {%>
                    <li class="list-group-item row"><%= billet.getCommentaires().get(j).toString()%></li>
                    <% } %>
                </ul>
            </div>
            <div class="card-footer text-muted container">
                <form method="post" action="commente">
                    <div class="form-row">
                        <div class="col-7">
                            <input type="hidden" name="name" class="form-control mb-2 mr-sm-2" id="inlineFormInputName2" value="<%= billet.getTitre() %>" >
                            <input type="text" name="commentaire" class="form-control mb-2 mr-sm-2" id="inlineFormInputName2" placeholder="Commentaire">
                        </div>
                        <div class="col">
                            <button type="submit" class="btn btn-primary mb-2">envoyer</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <%}%>
    </div>

</body>
</html>
