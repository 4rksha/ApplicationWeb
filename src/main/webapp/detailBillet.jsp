<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.GestionBillets" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.*" %>
<%! private GestionBillets gBillet = GestionBillets.getInstance();%>
<%! private Billet billet; %>
<%! private Integer id; %>
<% 
    if (!request.isRequestedSessionIdValid()) {
        response.sendRedirect("index.html");
    } else {
        if (request.getMethod().equals("POST")) {
            if (request.getParameter("commentaire") != null 
                    && request.getParameter("id") != null) {
                id = new Integer(request.getParameter("id"));
                gBillet.addCommantaireBillet(
                        (String) session.getAttribute("pseudo"),
                        id, 
                        request.getParameter("commentaire"));
                billet = gBillet.getBillet(id);
            }
        }
        if (request.getMethod().equals("GET")) {
            if (request.getParameter("id") != null){
                id = new Integer(request.getParameter("id"));
                billet = gBillet.getBillet(id);
            }
            else { }
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
    <meta http-equiv="refresh" content="5;url=detailBillet.jsp?id=<%=id%>" />
    <title><%= billet.getTitre() %></title>
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
                <li class="nav-item">
                    <a class="nav-link" href="billet.jsp">Accueil</a> 
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
        <h1 class="text-left"><%= billet.getTitre()%></h1>
        <br/>
        <div class="card" style="max-width: 600px;">
            <div class="card-body">
                <p class="card-text"><%= billet.getContenu()%></small></p>
                <p class="card-text"><small class="text-muted">écrit par <%= billet.getAuteur() %></small></p>
            </div>
        </div>
        <div>
            <br/>
            <h5 class="text-left">Commentaire</h5>
            <br/>
            <ul class="list-group list-group-flush">
                <li class="list-group-item">
                    <form method="post" action="commente">
                        <div class="form-row">
                            <div class="col-7">
                                <input type="hidden" name="id" class="form-control mb-2 mr-sm-2" id="inlineFormInputName2" value="<%= id %>" >
                                <input type="text" name="commentaire" class="form-control mb-2 mr-sm-2" id="inlineFormInputName2" placeholder="Commentaire">
                            </div>
                            <div class="col">
                                <button type="submit" class="btn btn-primary mb-2">envoyer</button>
                            </div>
                        </div>
                    </form>
                </li>
                <% for (int j = 0; j < billet.getCommentaires().size(); j++) {%>
                    <li class="list-group-item"><%= billet.getCommentaires().get(j).toString()%></li>
                <% } %>
            </ul>
        </div>
    </div>
</body>
</html>
