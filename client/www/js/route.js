const PSEUDO = 'pseudo';
const GROUPE = "groupe";
const BILLET = "billet";

function init() {
    let page = window.location.hash;
    // let page = "";
    // let context = "";
    // if (/^(#[a-zA-Z1-9]+)\/?(.*)$/.test(appUrl)) {
    //     page = RegExp.$1;
    //     context = RegExp.$2;
    // }
    let pseudo = localStorage.getItem("pseudo");
    let urlGroupe = localStorage.getItem(GROUPE);
    $("section").hide();
    if (page != "" && pseudo != null) {
        switch (page) {
            case "#groupes":
                groupes();
                break;
            case "#groupe":
                if (urlGroupe == null) {
                    location.hash = "#groupes";
                    break;
                }
                groupe(urlGroupe);
                break;
            case "#billet":
                let urlBillet = localStorage.getItem(BILLET);
                if (urlBillet == null){
                    location.hash = "#groupe";
                    break;
                }
                billet(urlBillet);
                break;
            case "#users":
                if (urlGroupe == null) {
                    location.hash = "#groupes";
                    break;
                }
                users(urlGroupe);
                break;
            default:
                break;
        }
        $(page).show();
    } else {
        $("#index").show();
        window.location.hash = "#index";
    }
}

/**
 * Télécharge et affiche la liste des groupes
 */
function groupes() {
    let data = JSON.parse('["http://localhost:8080/groupes/M1IF03","http://localhost:8080/groupes/M1IF04"]');
    let groupesResult = [];
    for (let groupe of data) {
        if (/^.*\/(.+)$/.test(groupe)) {
            groupesResult.push({ name: RegExp.$1, url: groupe });
        }
    }
    let template = $("#groupesTemplate").html();
    let text = Mustache.render(template, groupesResult);
    $("#groupesList").html(text);
}

/**
 * Redirige vers la page du groupe
 * @param {String} urlGroupe 
 */
function join(urlGroupe) {
    localStorage.setItem(GROUPE, urlGroupe);
    location.hash = "#groupe";
}
/**
 * Télécharge les billets fournie en url dans le tableau
 * @param {String[]} urlTabBillets 
 * @returns tableau de billets
 */
function getSummuryBillets(urlTabBillets) {
    let billets = []
    for (urlbillet of urlTabBillets) {
        $.ajax({
            url: "./js/billet.json",
            dataType: 'json',
            async: false,
            success: function (data) {
                if (/^.*\/(.+)$/.test(urlbillet)) {
                    billets.push({ id: RegExp.$1, titre: data.titre, url: urlbillet });
                }
            }
        });
    }
    return billets;
}

/**
 * Télecharge et affiche un groupe
 * @param {String} urlGroupe liens du groupe
 */
function groupe(urlGroupe) {
    $.ajax({
        url: "./js/groupe.json",
        dataType: 'json',
        success: function (data) {
            $("#grpName").html("Groupe " + data.nom);
            $("#grpDesc").html(data.description);
            let template = $("#SbilletsTemplate").html();
            let text = Mustache.render(template, getSummuryBillets(data.billets));
            $("#bltList").html(text);
        }
    });
}

/**
 * Affiche un bouton
 * @param {String} idElement l'id du bouton à afficher
 */
function displayBtn(idElement) {
    $("#" + idElement).show();
}

/**
 * Envoie la modification du titre au serveur
 * @param {String} urlBillet url du billet
 * @param {String} idBillet id du billet modifié
 */
function save_titreB(urlBillet, idBillet) {
    let editedTitle = $("#titre_" + idBillet).text();
    //envoie
    $("#edit_" + idBillet).hide();
}

/**
 * Ouvre la page d'un billet
 * @param {String} urlBillet 
 */
function openBillet(urlBillet){
    localStorage.setItem(BILLET,urlBillet);
    location.hash = "#billet";
}
/**
 * Télécharge et affiche un billet
 * @param {String} urlBillet url du billet à afficher
 */
function billet(urlBillet) {
    $.ajax({
        url: "./js/billet.json",
        dataType: 'json',
        async: false,
        success: function (data) {
            $("#bltAuteur").text(data.auteur);
            $("#bltTitre").text(data.titre);
            $("#bltContenu").text(data.contenu);
            let template = $("#commentairesTemplate").html();
            let text = Mustache.render(template, commaintaires(data.commentaires));
            $("#commentList").html(text);
        }
    });
}

/**
 * Télécharge les commentaires du tableau forunit
 * @param {String[]} urlTab Tableau d'url de commentaire
 * @returns Le tableau des commantaire téléchargé
 */
function commaintaires(urlTab) {
    result = []
    // for (let url of urlTab) { // A mettre a la place de in 
    for (let url in urlTab) {
        $.ajax({
            url: "./js/commentaires/" + url + ".json",
            dataType: 'json',
            async: false,
            success: function (data) {
                // if (/^.*\/(.+)$/.test(url)) { // version avec le of 
                if (/^.*\/(.+)$/.test(urlTab[url])) {
                    result.push({ id: RegExp.$1, texte: data.texte, auteur: data.auteur });
                }
            }
        });
    }
    return result;
}

/**
 * Upload la modification du commentaire
 * @param {String} urlCom url du commentaire
 * @param {String} idCom id du commentaire
 * @param {String} author auteur
 */
function save_commentaire(urlCom, idCom, author) {
    if (localStorage.getItem(PSEUDO) == author) {
        let newText = $("text_" + idCom).text();
        // send 
    } else {
        //erreur afficher dans errMsg
    }
    $("#editC_" + idCom).hide();
}

/**
 * Télécharge et affiche la liste des utilisateur d'un groupe
 * @param {String} urlGroupe 
 */
function users(urlGroupe) {
    $.ajax({
        url: "./js/groupe.json", // exemple
        dataType: 'json',
        success: function (data) {
            let template = $("#usersTemplate").html();
            let text = Mustache.render(template, data.membres);
            $("#usersList").html(text);
        }
    });
}

function formPseudo(event) {

    event.preventDefault();

}
/**
 * Chargement du DOM
 */
$(function () {
    init();
    $(window).on("hashchange", init);
    $("#pseudoForm").submit(formPseudo);
});
