const PSEUDO = 'pseudo';
const GROUPE = "groupe";
const BILLET = "billet";

let billetCache = []; // cache des billets affiché dans la vue
let commentaireCacheMap = new Map(); // Map contenant les commentaires
let refreshBillets = false; // indique si on est en cours d'actualisation des billets
let refreshCommentaire = false; // indique si on est en cours d'actualiser des commentaires

function init() {
    let page = window.location.hash;
    let pseudo = localStorage.getItem(PSEUDO);
    let urlGroupe = localStorage.getItem(GROUPE);

    // On stop l'actualisation des données
    refreshBillets = false;
    refreshCommentaire = false;

    $("section").hide();
    if (page != "" && pseudo != null && pseudo !== "") {
        switch (page) {
            case "#groupes":
                groupes();
                break;
            case "#groupe":
                if (urlGroupe == null) {
                    location.hash = "#groupes";
                    break;
                }
                refreshBillets = true;
                billetCache = [];
                groupe(urlGroupe, true);
                break;
            case "#billet":
                let urlBillet = localStorage.getItem(BILLET);
                if (urlBillet == null) {
                    location.hash = "#groupe";
                    break;
                }
                refreshCommentaire = true;
                billet(urlBillet, true);
                break;
            case "#users":
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

/**
 * Télécharge et affiche la liste des groupes
 */
function groupes() {
    let target = "https://192.168.75.13/api/v2/groupes"
    $.ajax({
        url: target,
        type: 'GET',
        crossDomain: true,
        xhrFields: { withCredentials: true },
        headers: {
            "Accept": "application/json"
        }
    }).done(data => {
        console.log("SUCCESS : " + target);
        let groupesResult = [];
        for (let groupe of data) {
            if (/^.*\/(.+)$/.test(groupe)) {
                groupesResult.push({ name: RegExp.$1, url: groupe });
            }
        }
        // console.log(groupesResult);
        let template = $("#groupesTemplate").html();
        let text = Mustache.render(template, groupesResult);
        $("#groupesList").html(text);
    }).fail(xhr => {
        errorMsg("Erreur GET : " + target);
        console.log(xhr.responseText);
    });
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
 * Télécharge un billet
 * @param {String} urlBillet 
 * @return un billet
 */
function downloadBillet(urlBillet) {
    return new Promise(resolve => {
        $.ajax({
            url: urlBillet,
            type: 'GET',
            crossDomain: true,
            xhrFields: { withCredentials: true },
            headers: {
                "Accept": "application/json"
            }
        }).done(data => {
            resolve(data);
        }).fail(xhr => {
            errorMsg("Erreur POST : " + urlBillet);
            console.log(xhr.responseText);
        });
    });
}

/**
 * Télécharge les billets fournie en url dans le tableau
 * @param {String[]} urlTabBillets 
 * @returns tableau de billets
 */
function getBillets(urlTabBillets) {
    return new Promise(async (resolve) => {
        if (urlTabBillets.length == 0) {
            resolve([]);
        }
        let billets = [];
        for (urlbillet of urlTabBillets) {
            let billet = await downloadBillet(urlbillet);
            if (/^.*\/(.+)$/.test(urlbillet)) {
                billets.push({
                    id: RegExp.$1,
                    titre: billet.titre,
                    url: urlbillet
                });
            }
        }
        resolve(billets);
    });
}

/**
 * Télecharge et affiche un groupe
 * @param {String} urlGroupe liens du groupe
 * @param {Bool} loop Indique si on appel la fonction un fois ou si l'on souhaite une actualisation toute les 5secondes
 */
function groupe(urlGroupe, loop = false) {
    $.ajax({
        url: urlGroupe,
        type: 'GET',
        crossDomain: true,
        xhrFields: { withCredentials: true },
        headers: {
            "Accept": "application/json"
        }
    }).done(async (data) => {
        $("#grpName").html("Groupe " + data.nom);
        $("#grpDesc").html(data.description);
        let urlbillets = data.billets;
        let billets = await getBillets(urlbillets);
        // On regarde si le données sont différente de celle afficher, dans le cas contraire on n'actualise pas la vue
        if (billets !== [] 
            && !(billets.length === billetCache.length && billets.every((value, index) => value.titre === billetCache[index].titre))) {
            billetCache = billets;
            let template = $("#SbilletsTemplate").html();
            let text = Mustache.render(template, billets);
            $("#bltList").html(text);
        }

    }).fail(xhr => {
        errorMsg("Erreur POST : " + urlGroupe);
        console.log(xhr.responseText);
    });
    if (refreshBillets && loop) {
        setTimeout(_ => groupe(urlGroupe, true), 5000);
    }
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
    let payload;
    let target = urlBillet;
    $.ajax({
        url: urlBillet,
        type: 'GET',
        crossDomain: true,
        xhrFields: { withCredentials: true },
        headers: {
            "Accept": "application/json"
        }
    }).done(data => {
        payload = data;
        let editedTitle = $("#titre_" + idBillet).text();
        if (editedTitle == payload.titre) {
            $("#edit_" + idBillet).hide();
            return;
        }
        payload.titre = editedTitle;
        $.ajax({
            url: target,
            type: 'PUT',
            crossDomain: true,
            data: JSON.stringify(payload),
            xhrFields: { withCredentials: true },
            headers: {
                "Content-Type": "application/json"
            }
        }).done(_ => {
            join(localStorage.getItem(GROUPE));
        }).fail(xhr => {
            errorMsg("Erreur PUT : " +  target);
            console.log(xhr.responseText);
        });
        $("#edit_" + idBillet).hide();
    }).fail(xhr => {
        errorMsg("Erreur GET : " +  target);
        console.log(xhr.responseText);
    });

}

/**
 * Ouvre la page d'un billet
 * @param {String} urlBillet 
 */
function openBillet(urlBillet) {
    localStorage.setItem(BILLET, urlBillet);
    location.hash = "#billet";
}

/**
 * Supprime un billet
 * @param {String} urlBillet 
 */
function deleteBillet(url) {
    $.ajax({
        url: url,
        type: 'DELETE',
        crossDomain: true,
        xhrFields: { withCredentials: true },
        headers: {
            "Content-Type": "application/json"
        }
    }).done(data => {
        console.log("SUCCESS : " + url);
        groupe(localStorage.getItem(GROUPE));
    }).fail(xhr => {
        errorMsg("Erreur POST : " + url);
        console.log(xhr.responseText);
    });
}

/**
 * Récupération des commentaires
 * @param {String[]} urlsCommentaire tableau contenant les url des commentaires
 * @return tableau de commentaires
 */
async function donwloadCommentaires(urlsCommentaire, param) {
    let promises = urlsCommentaire.map(url => fetch(url, param));
    let res = await Promise.all(promises);
    let responses = res.map(data => data.json());
    let commentaires = await Promise.all(responses);
    let results = [];
    for (let com of commentaires) {
        let t = com.auteur.split('/');
        let name = t[t.length - 1];
        let objCom = { id: commentaires.indexOf(com), texte: com.texte, auteur: name, url: urlsCommentaire[commentaires.indexOf(com)] };
        results.push(objCom);
        commentaireCacheMap.set(urlsCommentaire[commentaires.indexOf(com)], objCom);
    }
    return results;
}

/**
 * Télécharge et affiche un billet
 * @param {String} urlBillet url du billet à afficher
 * @param {Bool} loop Indique si on appel la fonction un fois ou si l'on souhaite une actualisation toute les 5secondes
 */
function billet(urlBillet, loop = false) {
    // parametrage du header pour les requêtes
    let myHeaders = new Headers();
    myHeaders.append('Accept', 'application/json');
    let param = {
        method: 'GET',
        mode: 'cors',
        headers: myHeaders,
        credentials: 'include'
    };
    fetch(urlBillet, param)
        .then(response => response.json())
        .then(async (data) => {
            if (/^.*\/(.+)$/.test(data.auteur)) {
                let user = "" + RegExp.$1;
                $("#bltAuteur").text(user);
            }
            $("#bltTitre").text(data.titre);
            $("#bltContenu").text(data.contenu);
            let urlsCommentaire = [];
            let commentaires = []
            for (let urlCom of data.commentaires) {
                if (commentaireCacheMap.has(urlCom)) {
                    commentaires.push(commentaireCacheMap.get(urlCom));
                } else {
                    urlsCommentaire.push(urlCom);
                }
            }
            commentaires = commentaires.concat(await donwloadCommentaires(urlsCommentaire, param));
            let template = $("#commentairesTemplate").html();
            let text = Mustache.render(template, commentaires);
            $("#commentList").html(text);
        });

    // Actualisation de la page des billets
    if (refreshCommentaire && loop) {
        setTimeout(_ => billet(urlBillet, true), 5000);
    }
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
        let comm = { "auteur": author, "text": newText };
        $.ajax({
            url: urlCom,
            type: 'PUT',
            crossDomain: true,
            data: comm,
            xhrFields: { withCredentials: true },
            headers: {
                "Content-Type": "application/json"
            }
        }).done(_=> {
            console.log("SUCCESS : " + target);
        }).fail(xhr => {
            errorMsg("Erreur POST : " + target);
            console.log(xhr.responseText);
        });
    } else {
        errorMsg("Seul l'auteur d'un commentaire peut le modifier.");
    }
    $("#editC_" + idCom).hide();
}
/**
 * Affiche une erreur
 * @param {String} message Le message à afficher
 */
function errorMsg(message){
    $("#errMsg").text(message);
    $("#errMsg").show();
    setTimeout(_=> $("#errMsg").hide(),3000);
}

/**
 * Supprime un commentaire
 * @param {String} url 
 */
function deleteCommentaire(url) {
    $.ajax({
        url: url,
        type: 'DELETE',
        crossDomain: true,
        xhrFields: { withCredentials: true },
        headers: {
            "Content-Type": "application/json"
        }
    }).done(_=> {
        console.log("SUCCESS : " + url);
        billet(localStorage.getItem(BILLET));
    }).fail(function (xhr, textStatus, errorThrown) {
        errorMsg("Erreur POST : " + url);
        console.log(xhr.responseText);
    });
}

/**
 * Télécharge et affiche la liste des utilisateur d'un groupe
 * @param {String} urlGroupe 
 */
function users(urlGroupe) {
    let target = "https://192.168.75.13/api/v2/users";
    $.ajax({
        url: target,
        type: 'GET',
        crossDomain: true,
        xhrFields: { withCredentials: true },
        headers: {
            "Accept": "application/json"
        }
    }).done(function (data) {
        let users = [];
        for (let user of data) {
            name = user.split("/")[user.split("/").length - 1];
            users.push(name);
        }
        let template = $("#usersTemplate").html();
        let text = Mustache.render(template, users);
        $("#usersList").html(text);
    });
}

/**
 * Chargement du dome
 */
$(function () {
    init();
    $(window).on("hashchange", init);

    $('#pseudoForm').submit((event) => {
        event.preventDefault();
        let target = "https://192.168.75.13/api/v2/users/login";
        let psd = $("#pseudo").val();
        console.log(psd);
        let requestData = JSON.stringify({ "pseudo": psd });
        $.ajax({
            url: target,
            type: 'POST',
            xhrFields: { withCredentials: true },
            crossDomain: true,
            data: requestData,
            datatype: "json",
            headers: {
                "Content-Type": "application/json",
            }
        }).done(function (data, textstatus, xhr) {
            console.log("SUCCESS : " + target);
            localStorage.setItem("pseudo", psd);
            window.location = "#groupes";
        }).fail(function (xhr, textStatus, errorThrown) {
            errorMsg("Erreur POST : " + target);
            console.log(xhr.responseText);
        });
    });

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
            datatype: "json",
            xhrFields: { withCredentials: true },
            headers: {
                "Content-Type": "application/json"
            }
        }).done(function (data) {
            console.log("SUCCESS : " + target);
            console.log("groupe : " + groupe);
            join(target + "/" + groupe);
        }).fail(function (xhr, textStatus, errorThrown) {
            errorMsg("Erreur POST : " + target);
            console.log(xhr.responseText);
        });
    });

    $('#newBilletForm').submit((event) => {
        event.preventDefault();
        let target = localStorage.getItem(GROUPE) + "/billets";
        let titre = $("#titreBillet").val();
        let text = $("#contenuBilletInput").val();
        let requestData = JSON.stringify({
            "titre": titre,
            "contenu": text,
            "auteur": localStorage.getItem("pseudo"),
            "commentaires": []
        });
        $.ajax({
            url: target,
            type: 'POST',
            crossDomain: true,
            data: requestData,
            datatype: 'json',
            xhrFields: { withCredentials: true },
            headers: {
                "Content-Type": "application/json"
            }
        }).done(function (data, status, xhr) {
            console.log("SUCCESS : " + target);
            groupe(localStorage.getItem(GROUPE));
        }).fail(function (xhr, textStatus, errorThrown) {
            errorMsg("Erreur POST : " + target);
            console.log(xhr.responseText);
        });
    });

    $('#addCommentaireForm').submit((event) => {
        event.preventDefault();
        let target = localStorage.getItem(BILLET) + "/commentaires";
        let text = $("#commentaireInput").val();
        let requestData = JSON.stringify({
            "auteur": localStorage.getItem("pseudo"),
            "texte": text,
        });
        $.ajax({
            url: target,
            type: 'POST',
            crossDomain: true,
            data: requestData,
            datatype: 'json',
            xhrFields: { withCredentials: true },
            headers: {
                "Content-Type": "application/json"
            }
        }).done(function (data) {
            console.log("SUCCESS : " + target);
            billet(localStorage.getItem(BILLET));
        }).fail(function (xhr, textStatus, errorThrown) {
            errorMsg("Erreur POST : " + target);
            console.log(xhr.responseText);
        });
    });

    $('#decoForm').submit((event) => {
        event.preventDefault();
        let target = "https://192.168.75.13/api/v2/users/logout";
        //let del = $("#userCheckbox").val();
        $.ajax({
            url: target,
            type: 'POST',
            crossDomain: true,
            datatype: 'json',
            xhrFields: { withCredentials: true },
            headers: {
                "Content-Type": "application/json"
            },
            success: function (data) {
                console.log("SUCCESS : " + target);
                localStorage.setItem("pseudo", "");
                location.hash = "#index";
            },
            error: function (xhr, textStatus, errorThrown) {
                errorMsg("Erreur POST : " + target);
                console.log(xhr.responseText);
            }
        });
    });
});
