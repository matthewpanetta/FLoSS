package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import client.User;

/* FriendDAO class:
 * 		This class is responsible for creating, checking, and deleting friendship statuses in the database.
 * 
 * Methods:
 * 		+ boolean create(User user, User friend)		: Will create a friendship between two users.
 * 				-TRUE if friendship created, FALSE if not created
 * 		
 * 		+ boolean isFriendsWith(User user, User friend)	: Will check to see if the user is friends with another user.
 * 				-TRUE if the two users are friends, FALSE if they are not.
 * 
 * 		+ List<User> getAllFriends(User user)			: Will return the user names of all friends with a specified user. The password field will be blank for each friend.
 * 				-ARRAYLIST of Users for every friend. Password field is blank.
 * 
 * 		+ boolean delete(User user, User friend)		: Will delete the friendship between two users.
 * 				-TRUE if friendship is deleted, FALSE if it still exists.
 */

public class FriendDAO {
	private DBConnection connection = DBConnection.getInstance();
	private PreparedStatement statement = null;
	private ResultSet result = null;
	
	public boolean create(User user, User friend) {
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("INSERT INTO friend (userName, friendName) VALUES(?,?);");
			statement.setString(1, user.getUserName());
			statement.setString(2, friend.getUserName());
			
			statement.executeUpdate();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
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
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isFriendsWith(User user, User friend) {
		boolean isFriend;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("SELECT * FROM friend WHERE (userName = \"" + user.getUserName() + "\" AND friendName = \"" + friend.getUserName() + "\")"
				+ "OR (userName = \"" + friend.getUserName() + "\" AND friendName = \"" + user.getUserName() + "\");");
			
			result = statement.executeQuery();
			
			
			if(!result.isBeforeFirst()) {
				isFriend = false;
			}
			
			else {
				isFriend = true;
			}
		}
		
		catch (SQLException e) {
			isFriend = false;
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
		
		return isFriend;
	}
	
	public List<User> getAllFriends(User user) {
		List<User> friendList = new ArrayList<User>();
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("SELECT * FROM friend WHERE userName = ? OR friendName = ?;");
			statement.setString(1, user.getUserName());
			statement.setString(2, user.getUserName());
			
			result = statement.executeQuery();
			
			while(result.next()) {
				User friend = new User(result.getString("friendName"), "");
				friendList.add(friend);
			}			
		}
		
		catch (SQLException e) {
			e.printStackTrace();
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
		
		return friendList;
	}
	
	public boolean delete(User user, User friend) {
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("DELETE FROM friend WHERE (userName = ? AND friendName = ?) OR (userName = ? AND friendName = ?);");
			statement.setString(1, user.getUserName());
			statement.setString(2, friend.getUserName());
			statement.setString(3, friend.getUserName());
			statement.setString(4, user.getUserName());
			
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
		FriendDAO fd = new FriendDAO();
		
		User user = new User("mp755", "test123");
		User friend = new User("Matt", "test");
		
		// COMMENT OUT LINES THAT YOU DO NOT WANT TO TEST! //
		
		//fd.create(user, friend);										// ADD FRIENDS
		
		/*System.out.println(fd.isFriendsWith(user, friend));			// TEST IF FRIENDS
		System.out.println(fd.isFriendsWith(friend, user));*/
		
		
		//fd.delete(user, friend);										// DELETE FRIENDS
		
		
		/* GET ALL FRIENDS */
		
		/*List<User> friendList = fd.getAllFriends(user);
		
		for(User u : friendList) {
			System.out.printf("Username: %s\tis friends with %s\n", u.getUserName(), user.getUserName());
		}*/
	}
}
