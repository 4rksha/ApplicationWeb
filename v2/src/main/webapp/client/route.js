function init() {
    let page = window.location.hash;
    $("section").hide();   
    if (page != ""){
        $(page).show();   
    } else {
        $("#index").show();
    }
}

$(function() {
    init();
    $(window).on("hashchange",init);
    $.put();
});
