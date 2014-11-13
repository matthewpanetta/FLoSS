package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import client.User;

/* UserDAO class:
 * 		This class is responsible for creating, reading, updating, and deleting database entries specific to a user.
 * 		The class uses a connection to the database, through DBConnection.
 *		 
 * 
 * 
 * 
 * 
 * 
 * 
 */

public class UserDAO {
	private DBConnection connection = DBConnection.getInstance();
	private PreparedStatement statement = null;
	private ResultSet result = null;
	
	public boolean create(User user) {
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("INSERT INTO user VALUES(?,?);");
			statement.setString(1, user.getUserName());
			statement.setString(2, user.getUserPassword());
			
			statement.executeUpdate();
		}
		
		catch (SQLException e) {
			return false;
		}
		
		finally {
			connection.close();
			try {
				if(statement != null) {
					statement.close();
				}
				
				if(result != null) {
					result.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	public User getUser(String userName) {
		User user;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("SELECT * FROM user WHERE userName LIKE(?);");
			statement.setString(1, userName);
			
			result = statement.executeQuery();
			
			
			if(!result.isBeforeFirst()) {
				user = null;
			}
			
			else {
				result.next();
				user = new User(result.getString(1), result.getString(2));		// this seems like bad practice...oh well!
			}
		}
		
		catch (SQLException e) {
			return null;
		}
		
		finally {
			connection.close();
			try {
				if(statement != null) {
					statement.close();
				}
				
				if(result != null) {
					result.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return user;
	}
	
	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<User>();
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("SELECT * FROM user;");
			
			result = statement.executeQuery();
			
			while(result.next()) {
				User user = new User(result.getString("userName"), result.getString("password"));
				userList.add(user);
			}			
		}
		
		catch (SQLException e) {
			return null;
		}
		
		finally {
			connection.close();
			try {
				if(statement != null) {
					statement.close();
				}
				
				if(result != null) {
					result.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return userList;
	}
	
	public boolean update(User user) {
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("UPDATE user SET password = ? WHERE userName LIKE(?);");
			statement.setString(1, user.getUserPassword());
			statement.setString(2, user.getUserName());
			
			statement.executeUpdate();
		} 
		catch (SQLException e) {
			return false;
		}
		
		finally {
			connection.close();
			try {
				if(statement != null) {
					statement.close();
				}
				
				if(result != null) {
					result.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return true;
		
	}
	
	public boolean delete(String userName) {
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("DELETE FROM user WHERE userName LIKE(?);");
			statement.setString(1, userName);
			
			statement.execute();
		} 
		
		catch (SQLException e) {
			return false;
		}
		
		finally {
			connection.close();
			try {
				if(statement != null) {
					statement.close();
				}
				
				if(result != null) {
					result.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return true;
		}
	
	// ---------- TEST CASE ---------- //
	public static void main(String[] args) {
		UserDAO uw = new UserDAO();
		User user = new User("mp755", "test123");
		User user2 = new User("mp755", "test");
		
		// COMMENT OUT ALL LINES THAT YOU DON'T WANT TO TEST! //
		
		//System.out.println(uw.create(user));						// CREATE USER
		//System.out.println(uw.delete(user.getUserName()));		// DELETE USER
		//System.out.println(uw.update(user2));						// UPDATE USER
		
		
		/* READ USER */
		
		/*User userTest = uw.getUser("mp755");
		System.out.printf("Username: %s\tPassword: %s\n", userTest.getUserName(), userTest.getUserPassword());
		*/
		
		
		/* GET ALL USERS */
		
		/*List<User> userList = uw.getAllUsers();
		
		for(User u : userList) {
			System.out.printf("Username: %s\tPassword: %s\n", u.getUserName(), u.getUserPassword());
		}*/
	}
}
