package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserWriter {
	private DBConnection connection = DBConnection.getInstance();
	private String userName;
	private String password;
	
	public UserWriter(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public boolean writeUser() {
		try {
			connection.connect();
			PreparedStatement statement = connection.getConnection().prepareStatement("INSERT INTO user VALUES(?,?);");
			statement.setString(1, userName);
			statement.setString(2, password);
			
			statement.execute();
			connection.close();
			return true;
			
		}
		catch (SQLException e) {
			return false;
		}
	}
	
	public boolean removeUser() {
		try {
			connection.connect();
			PreparedStatement statement = connection.getConnection().prepareStatement("DELETE FROM user WHERE userName LIKE(?);");
			statement.setString(1, userName);
			
			statement.execute();
			connection.close();
			return true;
		} 
		catch (SQLException e) {
			return false;
		}
	}
	
	public boolean alterPassword() {
		try {
			connection.connect();
			PreparedStatement statement = connection.getConnection().prepareStatement("UPDATE user SET password = ? WHERE userName LIKE(?);");
			statement.setString(1, password);
			statement.setString(2, userName);
			
			statement.execute();
			connection.close();
			return true;
		} 
		catch (SQLException e) {
			return false;
		}
	}
	
	// ---------- TEST CASE ---------- //
	public static void main(String[] args) {
		UserWriter uw = new UserWriter("mp755", "test123");
		
		// COMMENT OUT ALL LINES THAT YOU DON'T WANT TO TEST! //
		
		//System.out.println(uw.writeUser());		// ADD USER
		//System.out.println(uw.removeUser());		// REMOVE USER
		System.out.println(uw.alterPassword());		// ALTER PASSWORD
	}
}
