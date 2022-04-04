package pt.unl.fct.di.adc.firstwebapp.util;

public class RemoveUserData {

	public String username, tokenUsername, tokenID, creationDate, expirationDate;
	
	public RemoveUserData() {
		
	}
	
	public RemoveUserData(String username, String tokenUsername, String tokenID, String creationDate, String expirationDate) {
		this.username = username;
		this.tokenUsername = tokenUsername;
		this.tokenID = tokenID;
		this.creationDate = creationDate;
		this.expirationDate = expirationDate;
	}
	
}
