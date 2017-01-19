/**
 * Created by kuir on 1/13/2017.
 */


/* This code is to retrieve data from server openData.php gets data from mySQL database
sends back a json object. it's call jsonData but for the sake of this i will just hard code it.
Also after getting jsondata back I would have a function that would set all house function according
to the data. Like if the lights where left on previous it would set the lights on when you load the page.

// Open websocket
var ws = new WebSocket("ws://localhost:9998/openData.php");

ws.onopen = function()
{
    var get = {"method":"get"};
    ws.send(get);
};

ws.onmessage = function (evt)
{
    var jsonData = evt.data;
    alert("Message is received...");
};

ws.onclose = function()
{
    presetData(jsonData)
};

function presetData(jsonData)
{
    set the house functions according to the jsonData.
}



*/

 var jsonData =
    {
        "controls":
        {
            "lights":"off",
            "curtains":"close",
            "garage":"close",
            "door":"lock",
            "sprinkler":"off",
            "waterHeater":"off",
        },
        "data":
            {
                "tempOutside":0,
                "tempDesire":0
            }


    };



$(document).ready(function()
{
    $("#light").click(function()
    {
        $(".window, .window2").toggle(this.checked);
        if ($("#light").prop('checked'))
        {
            $("#light-on").css("backgrond-color", "#cc5337");
            jsonData.controls.lights = "on";
        }
        else
        {
            $("#light-on").cssRules("background-color", "#50cc26");
            jsonData.controls.lights="off";
        }
    });

    $("#curtainid").click(function()
    {
        if($("#curtainid").prop('checked')) {
            $(".curtain,.curtain2").animate({height: '50px'}, 800);
            $("#window-close").css("background-color", "#cc5337");
            jsonData.controls.curtains="on";
        }
        else {
            $(".curtain,.curtain2").animate({height: '5px'}, 800);
            $("#window-close").css("background-color", "#50cc26");
            jsonData.controls.curtains="off";
        }
    });

    $("#garageid").click(function()
    {
        if($("#garageid").prop('checked')) {
            $(".garage-door").animate({height: '10px'}, 2000);
            $("#garage-open").css("background-color", "#cc5337");
            jsonData.controls.garage="open";
        }
        else {
            $(".garage-door").animate({height: '68px'}, 2000);
            $("#garage-open").css("background-color", "#50cc26");
            jsonData.controls.garage="closed";
        }
    });

    $("#doorid").click(function()
    {
        if($("#doorid").prop('checked'))
        {
            $(".door").animate({width: '45px'}, "slow");
            $("#door-look").css("background-color", "#cc5337");
            jsonData.controls.door = "open";
        }
        else
        {
            $(".door").animate({width: '50px'}, "slow");
            $("#door-look").css("background-color", "#50cc26");
            jsonData.controls.door="closed";
        }

    });

    $("#sprinklers").click(function()
    {
        if($("#sprinklers").prop('checked')) {
            $("#sprinklers-on").css("background-color", "#cc5337");
            jsonData.controls.sprinkler="on";

            $(".sprinkler1").fadeIn(100);
            $(".sprinkler2").fadeIn(100);

           for(var t =0;t<100;t++)
            {
                $(".sp-water").fadeIn(200);
                $(".sp-water").fadeOut(400);
                $(".sp-water").fadeIn(400);
                $(".sp-water").fadeOut(200);
            }
        }
        else
        {
            $("#sprinklers-on").css("background-color", "#50cc26");
            $(".sprinkler1").css("display","none");
            $(".sprinkler2").css("display","none");
            jsonData.controls.sprinkler="off";
        }

    });

    $("#water-heater").click(function()
    {
        if($("#water-heater").prop('checked'))
        {
            $("#water-heater-on").css("background-color", "#cc5337");
            jsonData.controls.waterHeater = "on";
            for(var t =0;t<5;t++)
            {
                $(".smoke1").fadeIn(100);
                $(".smoke2").fadeIn(500);
                $(".smoke3").fadeIn(1000)
                $(".smoke1").fadeOut(1000);
                $(".smoke2").fadeOut(500);
                $(".smoke3").fadeOut(100)

            }
        }
        else
        {
            $("#water-heater-on").css("background-color", "#50cc26");
            jsonData.controls.waterHeater = "off";
        }

    });


    $("#therm").on("input",function ()
    {
        var newValue = this.value;
        jsonData.data.tempDesire = newValue;
        $('#newValue').html(newValue);
        for(var t =0;t<5;t++)
        {
            $(".smoke1").fadeIn(100);
            $(".smoke2").fadeIn(500);
            $(".smoke3").fadeIn(1000)
            $(".smoke1").fadeOut(1000);
            $(".smoke2").fadeOut(500);
            $(".smoke3").fadeOut(100)

        }
    });

});

var inFormOrLink;
$('a').on('click', function() { inFormOrLink = true; });
$('form').on('submit', function() { inFormOrLink = true; });

$(window).on("beforeunload", function()
{
    Send(jsonData);
    return inFormOrLink ? "Do you really want to close?" : null;
});









function Send(data)
{

        var ws = new WebSocket("ws://localhost:8080/saveData.php");

        ws.onopen = function()
        {
            ws.send(data);
        };

        ws.onmessage = function (evt)
        {
            var received_msg = evt.data;
        };

        ws.onclose = function()
        {
        };


}
