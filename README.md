# Projet TP2 et TP3 Conception d'applications Web

## URLs
* Url Tomcat client AJAX : https://192.168.75.87/client/
* Url Nginx client AJAX  : https://192.168.75.87/


## Relevé de performance

Pour la suite, on omettera de juger les résultats non significatif du à l'inconsistance du réseau (gain ou perte de l'ordre de quelques ms).

### Relevé de performance déploiement sur Tomcat 
||Temps|
|------|------|
|Temps de récupération de la page HTML |50.15 ms|
|Temps de chargement du CRP|212.15 ms|
|Temps de chargement du appShell|250.58 ms|

### Relevé de performance avec déploiement sur nginx 

| |Temps|Gain|Gain en %|
| ------ | ------ | ------ | ------ |
|Temps de récupération de la page HTML :|47.97 ms|2.18 ms|4.34%|
|Temps de chargement du CRP :|196.97 ms|15.18 ms|7.16%|
|Temps de chargement du appShell :|239.75 ms|10.83 ms|4.38%|

On observe de gain qui traduise un traitement plus rapide des requetes de la part du serveur

### Utilisation de CDN pour mustache.js

| |Temps|Gain|Gain en %|
| ------ | ------ | ------ | ------ |
|Temps de récupération de la page HTML :|48.04 ms|-|-|
|Temps de chargement du CRP :|174.04 ms|38.11 ms|17.96%|
|Temps de chargement du appShell :|241.58 ms|-|-|                 -         -

Un gain est observé sur le CRP. En revanche, l'appShell met plus de temps a se concrétiser.

### Utilisation de async/defer



| |Temps|Gain|Gain en %|
| ------ | ------ | ------ | ------ |
|Temps de récupération de la page HTML :|47.33 ms|-| -|
|Temps de chargement du CRP :|164.33 ms|47.82 ms|22.54%|
|Temps de chargement du appShell :|203.69 ms|47.89 ms|18.71%|

### Minification de route.js

route.js contient l'essentiel des traitements javascript lié à notre application 

| |Temps|Gain|Gain en %|
| ------ | ------ | ------ | ------ |
|Temps de récupération de la page HTML :|47.38 ms |-| -|
|Temps de chargement du CRP :|161.38 ms|50.77 ms|23.93%|
|Temps de chargement du appShell :|203.04 ms|-|-|

### Refactoring de l'application 

L'audit de google nous indique que les seuls ressources bloquantes sont : 
* bootstrap.min.css
* jquery.min.js


Cependant, notre application étant dépendante des données que ces fichiers contiennent, nous ne pouvons rien faire pour paralléliser leur chargement et risquer un affichage "cassé" de notre application.

Une autre solution serait de réduire la taille des fichiers envoyer par les CDN requétés.
En effet, chrome nous indique que nous n'utilisons que 4% du fichier bootstrap.min.css et 30% de jquery.min.css.
Nous n'avons malheureusement aucun moyen de requeter une version plus compacte de ces fichiers via CDN.
On pourrait vouloir faire la modification nous-même et stocker ces fichiers sur notre serveur, mais on perdrait alors l'utilité du CDN.

Pour finir l'audit de chrome (lighthouse) nous indique un score de performance de 100 et un score de best practices de 100 sur notre application.

Les gains finaux réalisé sur notre application sont les suivants :

||Gain|
| ------ | ------ |
|Récupération de la page HTML | 4.34%|
|Chargement du CRP | 23.93%|
|Chargement de l'appShell | 18.71%|

