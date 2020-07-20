// When the user hits return, send the "text-entered"
// message to main.js.
// The message payload is the contents of the edit box.
/*
var textArea = document.getElementById("edit-box");
textArea.addEventListener('keyup', function onkeyup(event) {
  if (event.keyCode == 13) {
    // Remove the newline.
    text = textArea.value.replace(/(\r\n|\n|\r)/gm,"");
    self.port.emit("text-entered", text);
    textArea.value = '';
  }
}, false);
*/
// Listen for the "show" event being sent from the
// main add-on code. It means that the panel's about
// to be shown.
//
// Set the focus to the text area so the user can
// just start typing.
/*
self.port.on("show", function onShow() {
  textArea.focus();
});
*/

function randomString(length) {
	var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
	var string_length = length;
	var randomstring = '';
	for (var i = 0; i < string_length; i++) {
		var rnum = Math.floor(Math.random() * chars.length);
		randomstring += chars.substring(rnum, rnum + 1);
	}
	return randomstring;
}

$("#btnLogin").click(function() {
	var isValid = true;
	var username = $("#username_login");
	var password = $("#password_login");
	if (username.val().length < 1) {
		username.addClass("invalid_textbox");
        isValid = false;
    } else {
		username.removeClass("invalid_textbox");
    }
	if (password.val().length < 1) {
		password.addClass("invalid_textbox");
        isValid = false;
    } else {
		password.removeClass("invalid_textbox");
    }
	if (isValid) {
		var user = username.val();
		var pwd = password.val();
		var host = "http://192.168.157.1:8080/JsonServices"
		var address = 'Pooh-PC';
		var ip = '192.168.157.1';
		var id = user + pwd + address; //CryptoJS.SHA256(user + pwd + address).toString();
		var rnd = randomString(5);
		$.ajax({
			"type": "POST",
			"dataType": "json",
			"async": false,
			//"contentType": "application/json; charset=utf-8",
			"url": host + "/Login",
			"data": { 
						status : '1', 
						username : user, 
						password : pwd, 
						ip : ip, 
						userID : id, 
						N1 : rnd
					},
			"success": function (json) {
				if (json[0].IsExistsUser) {
					if (json[0].invalidIP) {
						var keyClient = json[0].Kc;
						var message = id  + "^" + address + "^" + json[0].N2 + "^" + json[0].Times;
						//alert("key = " + keyClient + "\nMessage = " + message);
						var aC = CryptoJS.HmacSHA1(message, keyClient).toString();
						$.ajax({
							"type": "POST",
							"dataType": "json",
							"async": false,
							"url": host + "/Login",
							"data": { 
										status : '2', 
										ip : ip, 
										userID : id, 
										N2 : json[0].N2, 
										times : json[0].Times, 
										authenicatorC : aC, 
										sac : json[0].SAC
									},
							"success": function (json) {
								if (!json[0].authentication) {
									alert("username หรือ password ไม่ถูกต้อง");
								} else if (json[0].isTimeOut) {
									alert("หมดเวลาการใช้งาน กรุณาเข้าสู่ระบบใหม่");
								} else {
									self.port.emit("loginSuccess", json[0].token);
									username.val("");
									password.val("");
									alert("เข้าสู่ระบบเรียบร้อย");
								}
							},
							"error": function (msg) {
								username.val("");
								password.val("");
								alert("เกิดข้อผิดพลาด"); //alert(JSON.stringify(msg));
							}
						});
					} else {
						username.val("");
						password.val("");
						alert("ข้อมูลไม่ถูกต้องกรุณากรอกข้อมูลใหม่");
					}
				} else {
					username.val("");
					password.val("");
					alert("username หรือ password ไม่ถูกต้อง");
				}
			},
			"error": function (msg) {
				username.val("");
				password.val("");
				alert("เกิดข้อผิดพลาด"); //alert(JSON.stringify(msg));
			}
		});
	}
});