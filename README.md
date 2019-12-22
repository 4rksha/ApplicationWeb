# Conception d'applications Web

## Participants au projet
* BEDDALIA ZACHARIA **p1917930**
* VASLIN PIERRE **p1914433**

## Déploiement des TPS sur notre VM

| **TP3** | http://192.168.75.87:8080/v1 |
| - | - |
| **TP4** | http://192.168.75.87:8080/v2 |
| **TP7** | https://192.168.75.87 |

## TP2 & TP3
### Déploiement
Url de l'application: [http://192.168.75.87:8080/v1](http://192.168.75.87:8080/v1)
### Description de l'application du TP2 & TP3 
L'application implémente toutes les fonctionnalités demandées dans le **TP2**:
* Un utilisateur peut:
    * rejoindre ou créer un groupe lors de la connexion
    * changer de groupe à n'importe quel moment via le menu
    * visualiser seulement les billets de son groupe
    * ouvrir une vue détail d'un billet en cliquant sur un billet dans la liste des billets d'un groupe
    * visualiser le détail d'un billet contenant : le contenue du billet et ses commentaires
    * saisir un billet
    * ajouter un commentaire sur un billet
    * se déconnecter via le bouton présent à cet effet dans le menu
* La page des billets d'un groupe et la page d'un billet s'actualisent toutes les 5 secondes pour actualiser le contenu afficher dans la page.
* Chaque billet appartient à un groupe

Dans **TP3** nous avons implémenté tout le TP c'est-à-dire:
* le pattern context
* le pattern MVC pull-based disponible [ici](https://forge.univ-lyon1.fr/p1914433/m1if03-2019/-/tags/TP3-1.2.1)
* le pattern MVC push-based
    > Pour la suite du TP et pour l'application présente sur la VM, nous avons utilisé le pattern MVC push-based.
* le contrôle de l'accès aux vues
* le pattern chaîne de responsabilité (authentification, autorisation)
* la gestion gestion du cache
    >Pour le système de gestion de cache nous avons utilisé les entêtes HTTP

*Tag du rendu du TP 2 et 3: [tag TP2/3](https://forge.univ-lyon1.fr/p1914433/m1if03-2019/-/tags/TP3-2.1)*

## TP4
### Déploiement
**URL** de l'application: [http://192.168.75.87:8080/v2](http://192.168.75.87:8080/v2/)
### Description de l'API
L'API implémentée possède quelques différences par rapport à celle fournie dans le sujet. Les modifications sont présentées dans ce [fichier](https://forge.univ-lyon1.fr/p1914433/m1if03-2019/blob/master/apiSwagger.yml) swagger et ont "(modifiée)" dans leurs titre. 
Nous avons modifiée les requêtes:
* **POST** /groupes : on ne renseigne pas la liste des membres.
* **PUT** /groupes/{groupeId} : on ne renseigne pas la liste des membres.
* **POST** /groupes/{groupeId}/billets : on ne renseigne pas la liste des commentaires.
* **PUT** /groupes/{groupeId}/billets/{billetId} : on ne renseigne pas la liste des commentaires.

Nous avons implémenté toutes les requêtes demandées dans le sujet et celles que nous avons modifié.
Chaque requête de type GET possède une représentation JSON et HTML.
Pour les requêtes non modifiées, elles suivent la description OpenAPI du TP.
Pour les requêtes modifiées voici des requêtes **cURL** d'exemple :
* **POST** /groupes
    ```
    curl --location --request POST 'http://192.168.75.87:8080/v2/groupes' \
    --header 'Authorization: token.pour.swagger' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "nom": "Groupe1",
        "description": "Groupe créé depuis JSON",
        "proprietaire": "pierre"
    }'
    ```
* **PUT** /groupes/{groupeId}
    ```
    curl --location --request PUT 'http://192.168.75.87:8080/v2/groupes/Groupe1' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: token.pour.swagger' \
    --data-raw '{
        "nom": "Groupe1",
        "description": "Groupe modifiè",
        "proprietaire": "pierre"
    }'
    ```
* **POST** /groupes/{groupeId}/billets  
    > Pour le test de la requête, il est nécessaire d'avoir exécuter une des requêtes précédentes pour éviter un statut 400 *Bad Request*.
    ```
    curl --location --request POST 'http://192.168.75.87:8080/v2/groupes/Groupe1/billets' \
    --header 'Authorization: token.pour.swagger' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "titre": "Billet1",
        "contenu": "un contenut",
        "auteur": "pierre"
    }'
    ```
* **PUT** /groupes/{groupeId}/billets/{billetId}
    > Pour le test de la requête, il est nécessaire d'avoir éxécuter la requête précédente pour éviter un statut 400 *Bad Request*.
    ```
    curl --location --request PUT 'http://192.168.75.87:8080/v2/groupes/Groupe1/billets/0' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: token.pour.swagger' \
    --data-raw '{
        "contenu": "Le billet est modifié"
    }'
    ```
> Dans les requêtes précédentes nous avons mis "token.pour.swagger" (token de test persistant), il est possible de le remplacer par le token fournis à la connexion.

*Tag du rendu du TP 4: [tag TP4](https://forge.univ-lyon1.fr/p1914433/m1if03-2019/-/tags/TP4)*

## TP5 & TP7
### Déploiement
Url de l'application client: [https://192.168.75.87](https://192.168.75.87)
### Description
* L’API requêtée par notre application est votre VM.
* Les requêtes de l’API implémentées par notre application client sont:
    * **GET** /groupes
    * **POST** /groupes
    * **GET** /groupes/{groupeId}
    * **POST** /groupes/{groupeId}/billets
    * **GET** /groupes/{groupeId}/billets/{billetId}
    * **PUT** /groupes/{groupeId}/billets/{billetId}
    * **DELETE** /groupes/{groupeId}/billets/{billetId} 
    * **POST** /groupes/{groupeId}/billets/{billetId}/commentaires
    * **GET** /groupes/{groupeId}/billets/{billetId}/commentaires/{commentaireId}
    * **PUT** /groupes/{groupeId}/billets/{billetId}/commentaires/{commentaireId}
    * **DELETE** /groupes/{groupeId}/billets/{billetId}/commentaires/{commentaireId}
    * **GET** /users
    * **POST** /users/login
    * **POST** /users/logout

* Les éléments constituant le CRP sont tous les éléments de la page index.html sans les contenus chargé par le javascript, c'est-à-dire que `groupesList`, `bltList`, `commentList`, `usersList` n'ont pas de contenu et que les titres sont vide. L'utilisateur voit seulement avec les elements du CRP l'élément `header` et l'élément `section #index` (*route.min.js* n'est pas asynchrone).

*Tag du rendu du TP 5: [tag TP5](https://forge.univ-lyon1.fr/p1914433/m1if03-2019/-/tags/TP5)*
 
### Rapport performance TP7

### Relevé de performance

Pour la suite, on omettera de juger les résultats non significatif dû à l'inconsistance du réseau (gain ou perte de l'ordre de quelques ms). Les relevés sont réalisé avec ce script : 
```javascript
console.log("Temps de récupération de la page html", window.performance.timing.responseEnd - window.performance.timeOrigin, "ms");
console.log("Temps de chargement du CRP", window.performance.timing.domInteractive - window.performance.timeOrigin, "ms");
console.log("Temps de chargement du appShell", window.performance.getEntriesByName("first-contentful-paint")[0].startTime, "ms");
```

#### Relevé de performance déploiement sur Tomcat 
||Temps|
|------|------|
|Temps de récupération de la page HTML |50.15 ms|
|Temps de chargement du CRP|212.15 ms|
|Temps de chargement du appShell|250.58 ms|

#### Relevé de performance avec déploiement sur nginx 

| |Temps|Gain|Gain en %|
| ------ | ------ | ------ | ------ |
|Temps de récupération de la page HTML :|47.97 ms|2.18 ms|4.34%|
|Temps de chargement du CRP :|196.97 ms|15.18 ms|7.16%|
|Temps de chargement du appShell :|239.75 ms|10.83 ms|4.38%|

On observe de gain qui traduise un traitement plus rapide des requêtes de la part du serveur.

#### Utilisation de CDN pour mustache.js

| |Temps|Gain|Gain en %|
| ------ | ------ | ------ | ------ |
|Temps de récupération de la page HTML :|48.04 ms|-|-|
|Temps de chargement du CRP :|174.04 ms|38.11 ms|17.96%|
|Temps de chargement du appShell :|241.58 ms|-|-|

Un gain est observé sur le CRP. En revanche, l'appShell met plus de temps à se concrétiser.

#### Utilisation de async/defer

| |Temps|Gain|Gain en %|
| ------ | ------ | ------ | ------ |
|Temps de récupération de la page HTML :|47.33 ms|-| -|
|Temps de chargement du CRP :|164.33 ms|47.82 ms|22.54%|
|Temps de chargement du appShell :|203.69 ms|47.89 ms|18.71%|

#### Minification de route.js

route.js contient l'essentiel des traitements javascript lié à notre application 

| |Temps|Gain|Gain en %|
| ------ | ------ | ------ | ------ |
|Temps de récupération de la page HTML :|47.38 ms |-| -|
|Temps de chargement du CRP :|161.38 ms|50.77 ms|23.93%|
|Temps de chargement du appShell :|203.04 ms|-|-|

#### Refactoring de l'application 

L'audit de google nous indique que les seules ressources bloquantes sont : 
* bootstrap.min.css
* jquery.min.js


Cependant, notre application étant dépendante des données que ces fichiers contiennent, nous ne pouvons rien faire pour paralléliser leur chargement et risquer un affichage "cassé" de notre application.

Une autre solution serait de réduire la taille des fichiers envoyer par les CDN requêtés.
En effet, chrome nous indique que nous n'utilisons que 4% du fichier bootstrap.min.css et 30% de jquery.min.css.
Nous n'avons malheureusement aucun moyen de requêter une version plus compacte de ces fichiers via CDN.
On pourrait vouloir faire la modification nous-mêmes et stocker ces fichiers sur notre serveur, mais on perdrait alors l'utilité du CDN.

Pour finir l'audit de chrome (lighthouse) nous indique un score de performance de 100 et un score de best practices de 100 sur notre application.

Les gains finaux réalisé sur notre application sont les suivants :

||Gain|
| ------ | ------ |
|Récupération de la page HTML | 4.34%|
|Chargement du CRP | 23.93%|
|Chargement de l'appShell | 18.71%|