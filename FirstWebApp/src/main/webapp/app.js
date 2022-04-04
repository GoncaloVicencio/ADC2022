
	function sendFormSignUp(){

		var sname = document.getElementById("signname").value;
		var susername = document.getElementById("signuser").value;
		var semail = document.getElementById("signemail").value;
		var spassword = document.getElementById("signpass").value;
		var spassword2 = document.getElementById("signpass2").value;
		var sprofile = document.getElementById("signprofile").value;
		var sNIF = document.getElementById("signNIF").value;
		var sPhoneNoFixed = document.getElementById("signphonefix").value;
		var sPhoneNoMobile = document.getElementById("signphonemob").value;
		
		var srole = "User";
		var sstate = "INATIVO";

		if(spassword == spassword2){
			
			var objectCompact = {name:sname, username:susername, email:semail, password:spassword, password2:spassword2, 
					profile:sprofile, NIF:sNIF, PhoneNoFixed:sPhoneNoFixed, PhoneNoMobile:sPhoneNoMobile, role:srole, state:sstate};
			var JSONtransform = JSON.stringify(objectCompact);

			var xmlhttp = new XMLHttpRequest();

			xmlhttp.open("POST", "https://ordinal-ember-344517.appspot.com/rest/register/v4", true);
			xmlhttp.setRequestHeader("Content-type", "application/json");
			xmlhttp.send(JSONtransform);
           
            xmlhttp.onreadystatechange = function(){

                if (xmlhttp.readyState == 4) {
                    if (xmlhttp.status == 200) {
                        alert("Registration successful. Log In to continue.")
                    } else if (xmlhttp.status == 500) {
                        alert("Transaction error. Possible invalid property value.");
                    } else if (xmlhttp.status == 501) {
                        alert("Invalid registration attemp. Missing username, name, email or password.");
                    } else if (xmlhttp.status == 400){
                        alert("User already exists.");
                    } else {
                        alert("An unexpected error has ocurred.");
                    }
                }
            }
			
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

		xmlhttp.open("POST", "https://ordinal-ember-344517.appspot.com/rest/login/v2", true);
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.send(JSONtransform);

        xmlhttp.onreadystatechange = function(){

			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					var newObj = JSON.parse(xmlhttp.responseText);

                    if(sessionStorage.getItem("username")!=null){
                        alert("Log out from current account successful.")
                    }

					sessionStorage.setItem("username", newObj.username);
					sessionStorage.setItem("role", newObj.role);
					sessionStorage.setItem("token", newObj.tokenID);
					sessionStorage.setItem("creationDate", newObj.creationDate);
					sessionStorage.setItem("expirationDate", newObj.expirationDate);

					window.location.assign('https://ordinal-ember-344517.appspot.com/userpage.html');

                    alert("Sucessfully logged in to account: " + newObj.username)
                } else if (xmlhttp.status == 403){
                    alert("Username or Password do not match.");
                } else {
                    alert("An unexpected error has ocurred.");
                }
			}
		}
	}


	function sendFormUpdatePassword(){
		if(sessionStorage.getItem("username") != null){
            var uusername = sessionStorage.getItem("username");
            var upassword = document.getElementById("Upass").value;
            var uoldPassword = document.getElementById("UoldPass").value;

            var objectCompact = {username:uusername, password:upassword, oldPassword:uoldPassword};
            var JSONtransform = JSON.stringify(objectCompact);

            var xmlhttp = new XMLHttpRequest();
            xmlhttp.open("POST", "https://ordinal-ember-344517.appspot.com/rest/updatepassword/v1", true);
            xmlhttp.setRequestHeader("Content-type", "application/json");
            xmlhttp.send(JSONtransform);
            
            xmlhttp.onreadystatechange = function(){

                if (xmlhttp.readyState == 4) {
                    if (xmlhttp.status == 200) {
                        alert("Sucessfully updated password in account.")
                    } else if (xmlhttp.status == 500) {
                        alert("Transaction error. Possible invalid property value.");
                    } else if (xmlhttp.status == 403){
                        alert("Passwords are the same. No action taken.");
                    } else if (xmlhttp.status == 400){
                        alert("Username or password do not match.");
                    } else {
                        alert("An unexpected error has ocurred.");
                    }
                }
            }
        } else {
            alert("Log in to Proceed.")
        }
	}

	function sendFormRemoveAccount(){
		if(sessionStorage.getItem("username") != null){
            var rusername = document.getElementById("Ruser").value;
            var musername = sessionStorage.getItem("username");
            var mtoken = sessionStorage.getItem("token");
            var mcreationDate = sessionStorage.getItem("creationDate");
            var mexpirationDate = sessionStorage.getItem("expirationDate");


            var objectCompact = {username:rusername, tokenUsername:musername, token:mtoken, creationDate:mcreationDate, expirationDate:mexpirationDate };
            var JSONtransform = JSON.stringify(objectCompact);

            var xmlhttp = new XMLHttpRequest();
            xmlhttp.open("POST", "https://ordinal-ember-344517.appspot.com/rest/removeuser/v1", true);
            xmlhttp.setRequestHeader("Content-type", "application/json");
            xmlhttp.send(JSONtransform);
            
            if (xmlhttp.readyState == 4) {
                if (xmlhttp.status == 200) {
                    if(rusername == sessionStorage.getItem("username")){
                        sessionStorage.clear();
                        alert("Logged Out Successfully.");
                    }
                    alert("Account removed successfully.");
                } else if (xmlhttp.status == 500) {
                    alert("Transaction error. Possible invalid property value.");
                } else if (xmlhttp.status == 403) {
                    alert("You do not have permission to take this action.");
                } else if (xmlhttp.status == 400) {
                    alert("Kept going until rollback.");
                } else {
                    alert("An unexpected error has ocurred.");
                }
            }
        } else {
         alert("Log in to Proceed.")
        }
	}

	window.onload = function showTokenFromUser() {
		if(sessionStorage.getItem("username") != null){
            document.getElementById("usershow").innerHTML = "" + sessionStorage.getItem("username");
            document.getElementById("tokenshow").innerHTML = "" + sessionStorage.getItem("token");
            document.getElementById("dateshow").innerHTML = "" + sessionStorage.getItem("creationDate") + " to " + sessionStorage.getItem("expirationDate");
		}
	}

    function logOutFromCurrentAccount(){
        if(sessionStorage.getItem("username") != null) {

            sessionStorage.clear();
            if(sessionStorage.getItem("username") == null){
                alert("Log out Successful!");

            } else {
                alert("Error ocurred. Log out failed.")
            }

        } else 
            alert("Not currently logged in.");
       
    }
