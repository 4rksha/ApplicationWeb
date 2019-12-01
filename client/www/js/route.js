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
                if (urlBillet == null) {
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
    let payload = "";
    let target = "https://192.168.75.13/api/v2/groupes"
    $.ajax({
        url: target,
        type: 'GET',
        crossDomain: true,
        xhrFields: { withCredentials: true },
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
function getSummaryBillets(urlTabBillets) {
    let billets = []
    for (urlbillet of urlTabBillets) {
        $.ajax({
            url: urlbillet,
            type: 'GET',
            crossDomain: true,
            async: false,
            xhrFields: { withCredentials: true },
            headers: {
                "Accept": "application/json"
            },
            success: function (data) {
                if (/^.*\/(.+)$/.test(urlbillet)) {
                    billets.push({ id: RegExp.$1, titre: data.titre, url: urlbillet });
                }
            }, 
            error: function (xhr, textStatus, errorThrown) {
                $(".errMsg").append("Erreur POST : " + urlbillet);
                console.log(xhr.responseText);
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
        xhrFields: { withCredentials: true },
        headers: {
            "Accept": "application/json"
        },
        success: function (data) {
            $("#grpName").html("Groupe " + data.nom);
            $("#grpDesc").html(data.description);
            let template = $("#SbilletsTemplate").html();
            let text = Mustache.render(template, getSummaryBillets(data.billets));
            $("#bltList").html(text);
        }, 
        error: function (xhr, textStatus, errorThrown) {
            $(".errMsg").append("Erreur POST : " + urlGroupe);
            console.log(xhr.responseText);
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
    let payload;
    $.ajax({
        url: urlBillet,
        type: 'GET',
        crossDomain: true,
        xhrFields: { withCredentials: true },
        headers: {
            "Accept": "application/json"
        },
        success: function (data) {
            payload = data;
        },
        error: function (xhr, textStatus, errorThrown) {
            $(".errMsg").append("Erreur POST : " + target);
            console.log(xhr.responseText);
        }
    });
    
    let editedTitle = $("#titre_" + idBillet).text();
    if (editedTitle === payload.titre) {
        $("#edit_" + idBillet).hide();
        return;
    }
    payload.titre = editedTitle;
    let target = urlBillet;
    $.ajax({
        url: target,
        type: 'PUT',
        crossDomain: true,
        data: payload,
        xhrFields: { withCredentials: true },
        headers: {
            "Content-Type": "application/json"
        },
        success: function (data) {
            console.log("SUCCESS : " + target);
            join(localStorage.getItem(GROUPE));
        },
        error: function (xhr, textStatus, errorThrown) {
            $(".errMsg").append("Erreur POST : " + target);
            console.log(xhr.responseText);
        }
    });


    $("#edit_" + idBillet).hide();
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
        },
        success: function (data) {
            console.log("SUCCESS : " + url);
            join(localStorage.getItem(GROUPE));
        },
        error: function (xhr, textStatus, errorThrown) {
            $(".errMsg").append("Erreur POST : " + url);
            console.log(xhr.responseText);
        }
    });
}


/**
 * Télécharge et affiche un billet
 * @param {String} urlBillet url du billet à afficher
 */
function billet(urlBillet) {

    $.ajax({
        url: urlBillet,
        type: 'GET',
        crossDomain: true,
        xhrFields: { withCredentials: true },
        headers: {
            "Accept": "application/json"
        },
        success: function (data) {
            $("#bltAuteur").text(data.auteur);
            $("#bltTitre").text(data.titre);
            $("#bltContenu").text(data.contenu);
            let template = $("#commentairesTemplate").html();
            let text = Mustache.render(template, commentaires(data.commentaires));
            $("#commentList").html(text);
        },
        error: function (xhr, textStatus, errorThrown) {
            $(".errMsg").append("Erreur POST : " + target);
            console.log(xhr.responseText);
        }
    });
}

/**
 * Télécharge les commentaires du tableau forunit
 * @param {String[]} urlTab Tableau d'url de commentaire
 * @returns Le tableau des commantaire téléchargé
 */
function commentaires(urlTab) {
    result = []
    for (let url of urlTab) {
        $.ajax({
            url: url,
            type: 'GET',
            crossDomain: true,
            xhrFields: { withCredentials: true },
            async: false,
            headers: {
                "Accept": "application/json"
            },
            success: function (data) {
                if (/^.*\/(.+)$/.test(url)) {
                    result.push({ id: RegExp.$1, texte: data.texte, auteur: data.auteur });
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                $(".errMsg").append("Erreur POST : " + target);
                console.log(xhr.responseText);
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
        let comm = {"auteur": author , "text": newText};
        $.ajax({
            url: urlCom,
            type: 'PUT',
            crossDomain: true,
            data: comm,
            xhrFields: { withCredentials: true },
            headers: {
                "Content-Type": "application/json"
            },
            success: function (data) {
                console.log("SUCCESS : " + target);
            },
            error: function (xhr, textStatus, errorThrown) {
                $(".errMsg").append("Erreur POST : " + target);
                console.log(xhr.responseText);
            }
        });    
    } else {
        $(".errMsg").append("Seul l'auteur d'un commentaire peut le modifier.");
    }
    $("#editC_" + idCom).hide();
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
        async: false,
        headers: {
            "Accept": "application/json"
        },
        success: function (data) {
            let users = [];
            for (let user of data) {
                name = user.split("/")[user.split("/").length - 1];
                users.push(name);
            }
            let template = $("#usersTemplate").html();
            let text = Mustache.render(template, users );
            $("#usersList").html(text);
        }
    });
}


/**
 * Chargement du dome
 */
$(function () {
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
            },
            success: function (data) {
                console.log("SUCCESS : " + target);
            },
            error: function (xhr, textStatus, errorThrown) {
                $(".errMsg").append("Erreur POST : " + target);
                console.log(xhr.responseText);
            }
        }).done(function (data, textstatus, xhr) {
            localStorage.setItem("pseudo", psd);
            window.location = "#groupes";
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
            },
            success: function (data) {
                console.log("SUCCESS : " + target);
                console.log("groupe : " + groupe);
                join(target + "/" + groupe);
            },
            error: function (xhr, textStatus, errorThrown) {
                $(".errMsg").append("Erreur POST : " + target);
                console.log(xhr.responseText);
            }
        });
    });

    $('#newBilletForm').submit((event) => {
        event.preventDefault();
        let target = localStorage.getItem(GROUPE) + "/billets";
        let titre = $("#titreBillet").val();
        let text =  $("#contenuBilletInput").val();
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
            },
            success: function (data, status, xhr) {
                console.log("SUCCESS : " + target);
            },
            error: function (xhr, textStatus, errorThrown) {
                $(".errMsg").append("Erreur POST : " + target);
                console.log(xhr.responseText);
            }
        });
    });

    $('#addCommentaireForm').submit((event) => {
        event.preventDefault();
        let target = localStorage.getItem(BILLET) + "/commentaires" ;
        let text =  $("#commentaireInput").val();
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
            },
            success: function (data) {
                console.log("SUCCESS : " + target);
                openBillet(localStorage.getItem(BILLET));
            },
            error: function (xhr, textStatus, errorThrown) {
                $(".errMsg").append("Erreur POST : " + target);
                console.log(xhr.responseText);
            }
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
            },
            error: function (xhr, textStatus, errorThrown) {
                $(".errMsg").append("Erreur POST : " + target);
                console.log(xhr.responseText);
            }
        });
    });

    init();
    $(window).on("hashchange", init);
});
