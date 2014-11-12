package database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import client.User;

public class UserLocator {
	private DBConnection connection = DBConnection.getInstance();
	private String userName;
	private User user;
	
	public UserLocator() {
		
	}
	
	public void setUser(String userName) {
		this.userName = userName;
	}
	
	public User search() {
		try {
			connection.connect();
			PreparedStatement statement = connection.getConnection().prepareStatement("SELECT * FROM user WHERE userName LIKE(?);");
			statement.setString(1, userName);
			
			ResultSet result = statement.executeQuery();
			
			
			if(!result.isBeforeFirst()) {
				user = null;
			}
			
			else {
				result.next();
				user = new User(result.getString(1), result.getString(2));		// this seems like bad practice...oh well!
			}
			
			connection.close();
			return user;
			
		}
		catch (SQLException e) {
			return null;
		}
	}
	
	// ---------- TEST CASE ---------- //
	public static void main(String[] args) {
		UserLocator ul = new UserLocator();
		ul.setUser("mp755");
		User user = ul.search();
		
		if(user != null) {
			System.out.println(user.getUserName() + "\n" + user.getUserPassword());
		}
	}
}
