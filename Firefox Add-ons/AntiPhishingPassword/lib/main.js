var data = require("sdk/self").data;
var tabs = require("sdk/tabs");

var loginDialog = require("sdk/panel").Panel({
	width: 250,
	height: 160,
	contentURL: data.url("loginDialog.html"),
	contentStyleFile: data.url("style.css"),
	contentScriptFile: [data.url("jquery-1.11.1.min.js"), 
						data.url("hmac-sha1.js"), 
						data.url("hmac-sha256.js"), 
						data.url("sha256.js"), 
						data.url("loginDialog.js")], 
	onHide: handleHide
});
//loginDialog.show();

var host = "http://localhost:8080/JsonServices";
var loginSuccess = false;

function handleHide() {
	console.log("handleHide");
	//button.state('window', {checked: false});
	if (!loginSuccess) {
		loginDialog.show();
	}
}

/*
var button = require("sdk/ui/button/action").ActionButton({
  id: "show-panel",
  label: "Anti-Phishing Password Authentication",
  icon: {
    "16": "./anti-phishing-16.png",
    "32": "./anti-phishing-32.png",
    "64": "./anti-phishing-64.png"
  },
  onClick: handleClick
});
*/

function handleClick(state) {
	console.log("handleClick");
	loginSuccess = false;
	loginDialog.show();
	/*
	if (state.checked) {
		loginDialog.show({ position: button });
	}
	*/
}

/*
loginDialog.on("show", function() {
	loginDialog.port.emit("show");
});
*/

loginDialog.port.on("loginSuccess", function (token) {
	console.log("loginSuccess ");
	loginSuccess = true;
	loginDialog.hide();
	tabs.open(host + "/Login?Token=" + token);
	for (let tab of tabs) {
		console.log(tab.url);
		if (tab.url == host + "/Login") {
			tab.close();
		}
	}
	/*
	tabs.open({
	  url: "http://www.gamespot.com",
	  inBackground: true
	  //inNewWindow: false
	});
	*/
});

loginDialog.port.on("login", function () {
	console.log("login ");
	loginSuccess = false;

});

/*
// Import the page-mod API
var pageMod = require("sdk/page-mod");
 
// Create a page mod
// It will run a script whenever a ".org" URL is loaded
// The script replaces the page contents with a message
pageMod.PageMod({
  include: "*.org",
  contentScript: 'document.body.innerHTML = ' +
                 ' "<h1>Page matches ruleset</h1>";'
});
*/

/*
var pageMod = require("sdk/page-mod");
var contentScriptValue = 'self.port.emit("login");';

pageMod.PageMod({
  include: "http://localhost:8080/JsonServices/",
  contentScript: contentScriptValue
});
*/

/*
var pageModScript = "window.addEventListener('click', function(event) {" +
                    "  self.port.emit('click', event.target.toString());" +
                    "  event.stopPropagation();" +
                    "  event.preventDefault();" +
                    "}, false);" +
                    "self.port.on('warning', function(message) {" +
                    "window.alert(message);" +
                    "});"
*/

var pageModScript = "self.port.emit('login');"

var pageMod = require('sdk/page-mod').PageMod({
  include: host + "/Login", //['*']
  contentScript: pageModScript,
  onAttach: function(worker) {
    worker.port.on('login', function() {
    	loginSuccess = false;
		loginDialog.show();
    });
  }
});


tabs.on('activate', function () {
	console.log('active: ' + tabs.activeTab.url);
	if (tabs.activeTab.url == host + "/Login") {
		loginSuccess = false;
		loginDialog.show();
	} else {
		loginSuccess = true;
	}
});