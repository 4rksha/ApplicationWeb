const PSEUDO = 'pseudo';

localStorage.setItem("pseudo", "pierre");
function init() {
    let page = window.location.hash;
    let pseudo = localStorage.getItem("pseudo");
    $("section").hide();
    if (page != "" && pseudo != null) {
        switch (page) {
            case "#groupes":
                groupes();
                break;
            case "#groupe":
                // let urlGroupe = localStorage.getItem("groupeUrl");
                groupe();
                break;
            case "#billet":
                billet();
                break;
            case "#users":
                // let urlGroupe = localStorage.getItem("groupeUrl");
                users();
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

$(function () {

    $('#pseudoForm').submit(() => {
        let target = "https://192.168.75.13/api/v2/users/login";
        let pseudo = $("#pseudo").val();
        console.log(pseudo);
        let requestData = JSON.stringify({ "pseudo": pseudo });
        $.ajax({
            url: target,
            type: 'POST',
            xhrFields: {withCredentials: true},
            crossDomain: true,
            data: requestData,
            headers: {
                "Content-Type": "application/json",
            },
            success: function (data) {
                console.log("SUCCESS : " + target);
            },
            error: function (xhr, textStatus, errorThrown) {
                $(".errMsg").append("Erreur POST : " + target);
                console.log(xhr.responseText);
            }
        }).done(function (data, textstatus, xhr) {
            window.location = "#groupes";
        });
    });
});

/**
 * Télécharge et affiche la liste des groupes
 */
function groupes() {
    let payload = "";
    let target = "https://192.168.75.13/api/v2/groupes"
    $.ajax({
        url: target,
        type: 'GET',
        crossDomain: true,
        xhrFields: {withCredentials: true},
        headers: {
            "Accept": "application/json"
        },
        success: function (data) {
            console.log("SUCCESS : " + target);
            let groupesResult = [];
            for (let groupe of data) {
                if (/^.*\/(.+)$/.test(groupe)) {
                    groupesResult.push({ name: RegExp.$1, url: groupe });
                }
            }
            console.log(groupesResult);
            let template = $("#groupesTemplate").html();
            let text = Mustache.render(template, groupesResult);
            $("#groupesList").html(text);
        },
        error: function (xhr, textStatus, errorThrown) {
            $(".errMsg").append("Erreur GET : " + target);
            console.log(xhr.responseText);
        }
    });

}

function access(url) {
    let payload;
    $.ajax({
        url: url,
        type: 'GET',
        crossDomain: true,
        xhrFields: {withCredentials: true},
        headers: {
            "Accept": "application/json"
        },
        success: function (data) {
            payload = data;
        },
        error: function (xhr, textStatus, errorThrown) {
            console.log("erreur get : " + url);
            console.log(xhr.responseText);
        }
    });
}

$(function () {

    $('#addgroupeForm').submit((event) => {
        event.preventDefault();
        let target = "https://192.168.75.13/api/v2/groupes";
        let groupe = $("#groupeInput").val();
        let requestData = JSON.stringify({ "nom": groupe });
        $.ajax({
            url: target,
            type: 'POST',
            crossDomain: true,
            data: requestData,
            xhrFields: {withCredentials: true},
            headers: {
                "Content-Type": "application/json"
            },
            success: function (data) {
                console.log("SUCCESS : " + target);
                console.log ("groupe : " + groupe);
                window.location.hash = "#groupes/" + groupe;
            },
            error: function (xhr, textStatus, errorThrown) {
                $(".errMsg").append("Erreur POST : " + target);
                console.log(xhr.responseText);
            }
        });
    });
});






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
        url: urlGroupe,
        type: 'GET',
        crossDomain: true,
        xhrFields: {withCredentials: true},
        headers: {
            "Accept": "application/json"
        },
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
