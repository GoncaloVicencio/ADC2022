

	function sendFormSignUp(){

		var sname = document.getElementById("signname").value;
		var susername = document.getElementById("signuser").value;
		var semail = document.getElementById("signemail").value;
		var spassword = document.getElementById("signpass").value;
		var spassword2 = document.getElementById("signpass2").value;
		var sprofile = document.getElementById("signprofile").value;
		var sPhoneNoFixed = document.getElementById("signphonefix").value;
		var sPhoneNoMobile = document.getElementById("signphonemob").value;
		
		var srole = "User";
		var sstate = "INATIVO";

		if(spassword == spassword2){
			
			var objectCompact = {name:sname, username:susername, email:semail, password:spassword, password2:spassword2, 
					profile:sprofile, PhoneNoFixed:sPhoneNoFixed, PhoneNoMobile:sPhoneNoMobile, role:srole, state:sstate};
			var JSONtransform = JSON.stringify(objectCompact);

			var xmlhttp = new XMLHttpRequest();
			
			xmlhttp.open("POST", "https://ordinal-ember-344517.appspot.com/rest/register/v4", true);
			xmlhttp.setRequestHeader("Content-type", "application/json");
			xmlhttp.send(JSONtransform);
			
		} else{
			alert("Password and password confirmation do not match.")
		}

	}

	function sendFormLogIn(){
		
		var lusername = document.getElementById("loguser").value;
		var lpassword = document.getElementById("logpass").value;

		var objectCompact = {username:lusername, password:lpassword};
		var JSONtransform = JSON.stringify(objectCompact);

		var xmlhttp = new XMLHttpRequest();

		xmlhttp.onreadystatechange = function(){

			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					var newObj = xmlhttp.JSON.parse(xmlhttp.responsetext);

					sessionStorage.setItem("username", newObj.username);
					sessionStorage.setItem("role", newObj.role);
					sessionStorage.setItem("token", newObj.tokenID);
					sessionStorage.setItem("creationDate", newObj.creationDate);
					sessionStorage.setItem("expirationDate", newObj.expirationDate);

					window.location.replace('ordinal-ember-344517.appspot.com/userpage.html');
				} else {
					alert('status!=200');
				}
			}
		}
	
		xmlhttp.open("POST", "https://ordinal-ember-344517.appspot.com/rest/login/v3", true);
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(JSONtransform);

	}

	function sendFormUpdatePassword(){
		
		var uusername = document.getElementById("Uuser").value;
		var upassword = document.getElementById("Upass").value;
		var uoldPassword = document.getElementById("UoldPass").value;

		var objectCompact = {username:uusername, password:upassword, oldPassword:uoldPassword};
		var JSONtransform = JSON.stringify(objectCompact);

		var xmlhttp = new XMLHttpRequest();
		xmlhttp.open("POST", "https://ordinal-ember-344517.appspot.com/rest/updatepassword/v1", true);
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(JSONtransform);
	}

	function sendFormRemoveAccount(){
		
		var rusername = document.getElementById("Uuser").value;
		var musername = sessionStorage.getItem("username");
		var mrole = sessionStorage.getItem("role");
		var mtoken = sessionStorage.getItem("token");
		var mcreationDate = sessionStorage.getItem("creationDate");
		var mexpirationDate = sessionStorage.getItem("expirationDate");


		var objectCompact = {username:rusername, tokenUsername:musername, role:mrole, token:mtoken, creationDate:mcreationDate, expirationDate:mexpirationDate };
		var JSONtransform = JSON.stringify(objectCompact);

		var xmlhttp = new XMLHttpRequest();
		xmlhttp.open("POST", "https://ordinal-ember-344517.appspot.com/rest/removeuser/v1", true);
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(JSONtransform);
	}

	function userInPage(){
		if(sessionStorage.getItem("username")){
			document.getElementById("user").innerHTML = username;
		}
	}

