package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import client.PasswordHash;
import client.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* UserDAO class:
 * 		DAO = Data Access Object, a Java EE design pattern for interacting with a database.
 * 
 * 		This class is responsible for creating, reading, updating, and deleting database entries specific to a user.
 * 		The class uses a connection to the database, through DBConnection.
 *		 
 * 
 * 	Methods:
 * 		+ boolean create(User user) 		: Will create a new user entry in the database
 * 				-TRUE if creation successful, FALSE if unsuccessful
 * 
 * 		+ User getUser(String userName) 	: Will retrieve a specified user from the database
 * 				-USER object with generated fields from database
 * 
 * 		+ List<User> getAllUsers()			: Will retrieve every user in the database in an ArrayList
 * 				-ARRAYLIST of every user in the database.
 * 
 * 		+ boolean update(User user)			: Will alter a user's password. (Can technically alter userName as well, but this should not be supported.
 * 				-TRUE if update successful, FALSE if unsuccessful
 * 
 * 		+ boolean authenticate(User user)	: Will check a user's username and password against their supplied credentials. Will update checkIn time in database.
 * 				-TRUE if credentials match, FALSE if they do not.
 * 
 *  	+ boolean deauthenticate(User user)	: Will log a user out. Will update checkOut time in database.
 *  			-TRUE if user is logged out, FALSE if they are still logged in.
 *  
 *  	+ boolean isOnline(String userName)	: Will check to see if a user is currently online.
 *  			-TRUE if the user is online, FALSE if they are offline.
 * 
 * 		+ boolean delete(String userName)	: Will delete a specified user from the database
 * 				-TRUE if delete successful, FALSE if unsuccessful
 */

public class UserDAO {
	private DBConnection connection = DBConnection.getInstance();
	private PreparedStatement statement = null;
	private ResultSet result = null;
        private ResultSet passResult = null;
	
	public boolean create(User user) {
		try {
			java.sql.Date tempDate = new java.sql.Date(user.getBirthDate().getTime());
			
			connection.connect();
			statement = connection.getConnection().prepareStatement("INSERT INTO user (userName, password, firstName, middleInitial, lastName, gender, birthDate, email) VALUES(?,?,?,?,?,?,?,?);");
			statement.setString(1, user.getUserName());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getFirstName());
			statement.setString(4, user.getMiddleInitial());
			statement.setString(5, user.getLastName());
			statement.setString(6, user.getGender());
			statement.setDate(7, tempDate);
			statement.setString(8, user.getEmail());
			
			statement.executeUpdate();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
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
				
				Date tempDate = new Date(result.getDate("birthDate").getTime());
				
				user = new User(result.getString("userName"), result.getString("password"), result.getString("firstName"), result.getString("middleInitial"), 
					result.getString("lastName"), result.getString("gender"), result.getString("email"), tempDate);
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
				Date tempDate = new Date(result.getDate("birthDate").getTime());
				
				User user = new User(result.getString("userName"), result.getString("password"), result.getString("firstName"), result.getString("middleInitial"), 
					result.getString("lastName"), result.getString("gender"), result.getString("email"), tempDate);
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
			java.sql.Date tempDate = new java.sql.Date(user.getBirthDate().getTime());
			connection.connect();
			statement = connection.getConnection().prepareStatement("UPDATE user SET userName = ?, password = ?, firstName = ?, middleInitial = ?, lastName = ?, gender = ?, birthDate = ?, email = ? WHERE userName LIKE(?);");
			statement.setString(1, user.getUserName());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getFirstName());
			statement.setString(4, user.getMiddleInitial());
			statement.setString(5, user.getLastName());
			statement.setString(6, user.getGender());
			statement.setDate(7, tempDate);
			statement.setString(8, user.getEmail());
			statement.setString(9, user.getUserName());
			
			statement.executeUpdate();
		} 
		catch (SQLException e) {
			e.printStackTrace();
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
	
	public boolean authenticate(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
		boolean authenticated = false;
		
		try {
			connection.connect();
			//PreparedStatement statement = connection.getConnection().prepareStatement("SELECT * FROM user WHERE userName = ? AND password = ?;");
			//statement.setString(1, user.getUserName());
			//statement.setString(2, user.getPassword());
			//statement.setString(2, PasswordHash.createHash(user.getPassword()));
                        
                        PreparedStatement statement = connection.getConnection().prepareStatement("SELECT * FROM user WHERE userName = ?;");
			statement.setString(1, user.getUserName());
		
			result = statement.executeQuery();
                        result.next();
			String key = result.getString(2);
                        System.out.println(key);
			
			if(PasswordHash.validatePassword(user.getPassword(), key)){
                                //PreparedStatement getPass = connection.getConnection().prepareStatement("SELECT  password FROM user WHERE username = ?;");
                               // getPass.setString(1, user.getUserName());
                               // passResult = getPass.executeQuery();
                                
                                // user exists in database, now time to check the hashes
                                // The reason I can't check the password in the same way as before above is because the hashes are salted
                                // meaning that you have to use PasswordHash.validateHash() and you can't just hash it and compare
                                // because due to the salted nature of these hashes, the same string 'test' will hash to different values
                                // based on the random salt
                                
                                    authenticated = true;
				
                                    long currentTimestamp = System.currentTimeMillis();
                                    Timestamp timestamp = new Timestamp(currentTimestamp);
				
				
                                    statement = connection.getConnection().prepareStatement("UPDATE user SET checkIn = ? WHERE userName = ?;");
                                    statement.setTimestamp(1, timestamp);
                                    statement.setString(2, user.getUserName());
				
                                    statement.executeUpdate();
                                
                                
			} else {
				authenticated = false;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			authenticated = false;
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
		
		return authenticated;
	}
	
	public boolean deauthenticate(User user) {
		boolean deauthenticated;
		
		try {
			long currentTimestamp = System.currentTimeMillis();
			Timestamp timestamp = new Timestamp(currentTimestamp);
			
			connection.connect();			
			
			statement = connection.getConnection().prepareStatement("UPDATE user SET checkOut = ? WHERE userName = ?;");
			statement.setTimestamp(1, timestamp);
			statement.setString(2, user.getUserName());
			
			statement.executeUpdate();
			deauthenticated = true;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			deauthenticated = false;
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
		
		return deauthenticated;
	}
	
	public boolean isOnline(String userName) {
		boolean isOnline;
		
		try {			
			connection.connect();			
			
			statement = connection.getConnection().prepareStatement("SELECT * FROM user WHERE userName = ?");
			statement.setString(1, userName);
			
			result = statement.executeQuery();
			
			if(result.isBeforeFirst()) {
				result.next();
				
				Timestamp checkIn = result.getTimestamp("checkIn");
				Timestamp checkOut = result.getTimestamp("checkOut");
				
				isOnline = checkIn.after(checkOut);				
			} else {
				isOnline = false;
			}			
		}
		catch (SQLException e) {
			e.printStackTrace();
			isOnline = false;
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
		
		return isOnline;
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
		
		long dateMillis = 762238860000L;
		Date date = new Date(dateMillis);
		
		User user = new User("mp755", "test123", "Matthew", "D", "Panetta", "M", "mdp5280@psu.edu", date);
		
		
		// COMMENT OUT ALL LINES THAT YOU DON'T WANT TO TEST! //
		
		//System.out.println(uw.create(user));						// CREATE USER
		//System.out.println(uw.delete(user.getUserName()));		// DELETE USER
		
		/* UPDATE USER */
		/*user.setPassword("test123");
		user.setEmail("test@test.com");
		System.out.println(uw.update(user));
		*/
			
		
		/* READ USER */
		
		/*User userTest = uw.getUser("mp755");
		System.out.printf("Username: %s\t\nPassword: %s\nGender: %s\nEmail: %s\n", userTest.getUserName(), userTest.getPassword(), userTest.getGender(), userTest.getEmail());
		*/
		
		/* GET ALL USERS */
		
		/*List<User> userList = uw.getAllUsers();
		
		for(User u : userList) {
			System.out.printf("Username: %s\t\nPassword: %s\nGender: %s\nEmail: %s\n", u.getUserName(), u.getPassword(), u.getGender(), u.getEmail());
		}*/
		
		/* AUTHENTICATION CHECK USER */
		//uw.authenticate(user);
		//uw.deauthenticate(user);
		
		//System.out.println(uw.isOnline(user.getUserName()));
		
	}
}
