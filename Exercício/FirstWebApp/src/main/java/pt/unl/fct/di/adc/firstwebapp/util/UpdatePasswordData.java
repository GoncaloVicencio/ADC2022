package pt.unl.fct.di.adc.firstwebapp.util;

public class UpdatePasswordData {

	public String username, password, oldPassword;
	
	public UpdatePasswordData() {
		
	}
	
	public UpdatePasswordData(String username, String password, String oldPassword) {
		this.username = username;
		this.password = password;
		this.oldPassword = oldPassword;
	}
	
}
