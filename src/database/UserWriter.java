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
			return true;
			
		}
		catch (SQLException e) {
			return false;
		}
	}
	
	// ---------- TEST CASE ---------- //
	public static void main(String[] args) {
		UserWriter uw = new UserWriter("mp755", "test123");
		System.out.println(uw.writeUser());
	}
}
