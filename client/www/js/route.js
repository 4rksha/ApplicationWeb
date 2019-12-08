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
        }
    }).done(function (data) {
        console.log("SUCCESS : " + target);
        json = JSON.parse(data);
        let groupesResult = [];
        for (let groupe of json) {
            if (/^.*\/(.+)$/.test(groupe)) {
                groupesResult.push({ name: RegExp.$1, url: groupe });
            }
        }
        console.log(groupesResult);
        let template = $("#groupesTemplate").html();
        let text = Mustache.render(template, groupesResult);
        $("#groupesList").html(text);
    }).fail(function (xhr, textStatus, errorThrown) {
        $(".errMsg").append("Erreur GET : " + target);
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
        }
    }).done(function (data) {
        $("#grpName").html("Groupe " + data.nom);
        $("#grpDesc").html(data.description);
        let template = $("#SbilletsTemplate").html();
        let urlbillets = data.billets;
        let billets = [];
        var promise1 = new Promise(function (resolve, reject) {
            if (urlbillets.length == 0) {
                resolve([]);
            }
            let dic = [];
            for (urlbillet of urlbillets) {
                dic[urlbillet] =
                    $.ajax({
                        url: urlbillet,
                        type: 'GET',
                        crossDomain: true,
                        xhrFields: { withCredentials: true },
                        headers: {
                            "Accept": "application/json"
                        }
                    }).done(function (data) {
                        if (/^.*\/(.+)$/.test(urlbillet)) {
                            billets.push({
                                id: urlbillet.split("/")[urlbillet.split("/").length - 1],
                                titre: data.titre,
                                url: urlbillet
                            });
                        }
                        if (billets.length == urlbillets.length) {
                            resolve(billets);
                        }
                    }).fail(function (xhr, textStatus, errorThrown) {
                        $(".errMsg").append("Erreur POST : " + urlbillet);
                        console.log(xhr.responseText);
                    });
            }
        })
        promise1.then(function (value) {
            $("#bltList").html("");
            if (value !== []) {
                let text = Mustache.render(template, value);
                $("#bltList").html(text);
            }
        });

    }).fail(function (xhr, textStatus, errorThrown) {
        $(".errMsg").append("Erreur POST : " + urlGroupe);
        console.log(xhr.responseText);
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
        }
    }).done(function (data) {
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
            }
        }).done(function (data) {
            console.log("SUCCESS : " + target);
            join(localStorage.getItem(GROUPE));
        }).fail(function (xhr, textStatus, errorThrown) {
            $(".errMsg").append("Erreur POST : " + target);
            console.log(xhr.responseText);
        });
        $("#edit_" + idBillet).hide();
    }).done(function (xhr, textStatus, errorThrown) {
        $(".errMsg").append("Erreur POST : " + target);
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
    }).done(function (data) {
        console.log("SUCCESS : " + url);
        groupe(localStorage.getItem(GROUPE));
    }).fail(function (xhr, textStatus, errorThrown) {
        $(".errMsg").append("Erreur POST : " + url);
        console.log(xhr.responseText);
    });
}


/**
 * Télécharge et affiche un billet
 * @param {String} urlBillet url du billet à afficher
 */
function billet(urlBillet) {

    var myHeaders = new Headers();
    myHeaders.append('Accept', 'application/json');
    var param = {
        method: 'GET',
        mode: 'cors',
        headers: myHeaders,
        credentials: 'include'
    };

    fetch(urlBillet, param)
        .then(response => response.json())
        .then(async data => {
            $("#bltAuteur").text(data.auteur);
            $("#bltTitre").text(data.titre);
            $("#bltContenu").text(data.contenu);

            var promises = data.commentaires.map(url => fetch(url, param));
            const res = await Promise.all(promises);
            const responses = res.map(response => response.json());
            const responses_1 = await Promise.all(responses);
            var results = [];
            for (let resp of responses_1) {
                var t = resp.auteur.split('/');
                var name = t[t.length - 1];
                results.push({ id: responses_1.indexOf(resp), texte: resp.texte, auteur: name, url: data.commentaires[responses_1.indexOf(resp)] });
            }
            let template = $("#commentairesTemplate").html();
            let text = Mustache.render(template, results);
            $("#commentList").html(text);
            return results;
        });
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
                    }).done(function (data) {
                        console.log("SUCCESS : " + target);
                    }).fail(function (xhr, textStatus, errorThrown) {
                        $(".errMsg").append("Erreur POST : " + target);
                        console.log(xhr.responseText);
                    });
                } else {
                    $(".errMsg").append("Seul l'auteur d'un commentaire peut le modifier.");
                }
                $("#editC_" + idCom).hide();
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
                }).done(function (data) {
                    console.log("SUCCESS : " + url);
                    billet(localStorage.getItem(BILLET));
                }).fail(function (xhr, textStatus, errorThrown) {
                    $(".errMsg").append("Erreur POST : " + url);
                    console.log(xhr.responseText);
                });
            }/**
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
                        $(".errMsg").append("Erreur POST : " + target);
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
                        $(".errMsg").append("Erreur POST : " + target);
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
                        $(".errMsg").append("Erreur POST : " + target);
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
                        $(".errMsg").append("Erreur POST : " + target);
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
                            $(".errMsg").append("Erreur POST : " + target);
                            console.log(xhr.responseText);
                        }
                    });
                });

                init();
                $(window).on("hashchange", init);
            });
