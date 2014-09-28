$(function() {
    console.log("load api.js");
    
    $("#refreshButton").click(apiKeyRefresh);
    
    $.ajax({
        url: "api/apiKey",
        type: "GET"
    }).done(function(data, textStatus, jqXHR) {
        console.log("GET api/apiKey: status="+textStatus);
        
        $("#apiKeyText").html(data);
        $("#apiKeyLoading").hide();
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.log("GET api/apiKey: status="+textStatus);
        
        $("#apiKeyText").html("fail");
        $("#apiKeyLoading").hide();
    });
});

function apiKeyRefresh() {
    $("#apiKeyLoading").show();
    $("#apiKeyText").hide();
    
    $.ajax({
        url: "api/apiKey",
        type: "POST"
    }).done(function(data, textStatus, jqXHR) {
        console.log("POST api/apiKey: status="+textStatus);
        
        $.ajax({
            url: "api/apiKey",
            type: "GET"
        }).done(function(data, textStatus, jqXHR) {
            console.log("GET api/apiKey: status="+textStatus);
            
            $("#apiKeyText").show().html(data);
            $("#apiKeyLoading").hide();
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log("GET api/apiKey: status="+textStatus);
            
            $("#apiKeyText").show().html("fail");
            $("#apiKeyLoading").hide();
        });
    }).fail(function(data, textStatus, jqXHR) {
        console.log("POST api/apiKey: status="+textStatus);
        
        $("#apiKeyText").show().html("fail");
        $("#apiKeyLoading").hide();
    });
}
