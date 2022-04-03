package pt.unl.fct.di.adc.firstwebapp.util;

//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

public class RegisterData {

	public String username, password, password2, email;
	public String name, profile, PhoneNoFixed, PhoneNoMobile, NIF;
	public String role, state;
	
	public RegisterData() {
		
	}
	
	public RegisterData(String name, String username, String password, String password2, String email, 
			String profile, String PhoneNoFixed, String PhoneNoMobile, String NIF) {
		this.username = username;
		this.password = password;
		this.password2 = password2;
		this.email = email;
		this.name = name;
		this.profile = profile;
		this.PhoneNoFixed = PhoneNoFixed;
		this.PhoneNoMobile = PhoneNoMobile;
		this.NIF = NIF;
	}
	
	public void registerUser(String name, String username, String password, String password2, String email, 
			String profile, String PhoneNoFixed, String PhoneNoMobile, String NIF) {
		this.username = username;
		this.password = password;
		this.password2 = password2;
		this.email = email;
		this.name = name;
		this.profile = profile;
		this.PhoneNoFixed = PhoneNoFixed;
		this.PhoneNoMobile = PhoneNoMobile;
		this.NIF = NIF;
	}
	
	public void deleteUser(String username){
		if(!this.username.equals(username))
			return ;
		this.username = null;
		this.password = null;
		this.password2 = null;
		this.email = null;
		this.name = null;
		this.profile = null;
		this.PhoneNoFixed = null;
		this.PhoneNoMobile = null;
		this.NIF = null;
	}
	
	public boolean validRegistration() {
		//String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		
		//Pattern pattern = Pattern.compile(regex);

		//Matcher matcher = pattern.matcher(email);
		
		//if(matcher.matches())
			return  username != null && password != null;
		
	    //return false;
	}
}
