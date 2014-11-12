package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authenticator {
	private DBConnection connection = DBConnection.getInstance();
	private String userName;
	private String password;
	
	private static Authenticator instance = null;		// Singleton
	
	private Authenticator() {
		
	}
	
	public static Authenticator getInstance() {
		if(instance == null) {
			instance = new Authenticator();
		}
		return instance;
	}
	
	public void setCredentials(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public boolean authenticate() {
		try {
			boolean authenticated = false;
			connection.connect();
			PreparedStatement statement = connection.getConnection().prepareStatement("SELECT * FROM user WHERE userName LIKE(?) AND password LIKE (?);");
			statement.setString(1, userName);
			statement.setString(2, password);
			
			ResultSet result = statement.executeQuery();
			
			
			if(result.isBeforeFirst()) {
				authenticated = true;
			}
			
			connection.close();
			return authenticated;
			
		}
		catch (SQLException e) {
			return false;
		}
	}
	
	// ---------- TEST CASE ---------- //
	public static void main(String[] args) {
		Authenticator authenticator = Authenticator.getInstance();
		
		authenticator.setCredentials("mp755", "test123");				// SHOULD RETURN TRUE
		System.out.println(authenticator.authenticate());	
		
		authenticator.setCredentials("LOL", "LOL");						// SHOULD RETURN FALSE
		System.out.println(authenticator.authenticate());
	}
}
