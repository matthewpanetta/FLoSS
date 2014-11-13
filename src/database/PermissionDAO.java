package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import client.Permission;

/* PermissionDAO class
 * 		This class is responsible for creating, checking, and deleting permissions in the database.
 * 
 * Methods:
 * 		+ boolean create(Permission permission)				: Will create a permission in the database.
 * 			-TRUE if permission created, FALSE if permission not created.
 * 
 * 		+ boolean canAccess(String userName, int fileID)	: Will check to see if a user can access a specific file.
 * 			-TRUE if user has read OR write access, FALSE if user has no access.
 * 
 * 		+ boolean canWrite(String userName, int fileID)		: Will check to see if a user can write to a specific file.
 * 			-TRUE if user has write access, FALSE if user does not have write access.
 * 
 * 		+ List<Permission> getAccessList(String userName)	: Will return every permission that a specified user has.
 * 			-ARRAYLIST of Permission objects with generated fields.
 * 
 * 		+ boolean updateAccess(Permission permission)		: Will change the access level of a specified user and file. (Read -> Write  OR Write -> Read)
 * 			-TRUE if access updated, FALSE if not updated.
 * 
 * 		+ boolean delete(Permission permission)				: Will remove a permission from the database.
 * 			-TRUE if permission deleted, FALSE if permission still exists.
 */

public class PermissionDAO {
	private DBConnection		connection	= DBConnection.getInstance();
	private PreparedStatement	statement	= null;
	private ResultSet			result		= null;
	
	public boolean create(Permission permission) {
		boolean isCreated;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("INSERT INTO permission (userName, fileID, permissionType) VALUES(?,?,?);");
			
			statement.setString(1, permission.getUserName());
			statement.setInt(2, permission.getFileID());
			statement.setInt(3, permission.getPermissionType());
			
			statement.executeUpdate();
			isCreated = true;
		}
		
		catch (SQLException e) {
			isCreated = false;
		}
		
		finally {
			connection.close();
			try {
				if (statement != null) {
					statement.close();
				}
				
				if (result != null) {
					result.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return isCreated;
	}
	
	public boolean canAccess(String userName, int fileID) {
		boolean canAccess;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("SELECT * FROM permission WHERE userName = ? AND fileID = ?;");
			statement.setString(1, userName);
			statement.setInt(2, fileID);
			
			result = statement.executeQuery();
			
			if (!result.isBeforeFirst()) {
				canAccess = false;
			}
			
			else {
				canAccess = true;
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			canAccess = false;
		}
		
		finally {
			connection.close();
			try {
				if (statement != null) {
					statement.close();
				}
				
				if (result != null) {
					result.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return canAccess;
	}
	
	public boolean canWrite(String userName, int fileID) {
		boolean canAccess;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement(
				"SELECT * FROM permission WHERE userName = ? AND fileID = ? AND permissionType = 1;");
			statement.setString(1, userName);
			statement.setInt(2, fileID);
			
			result = statement.executeQuery();
			
			if (!result.isBeforeFirst()) {
				canAccess = false;
			}
			
			else {
				canAccess = true;
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			canAccess = false;
		}
		
		finally {
			connection.close();
			try {
				if (statement != null) {
					statement.close();
				}
				
				if (result != null) {
					result.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return canAccess;
	}
	
	public List<Permission> getAccessList(String userName) {
		List<Permission> accessList = new ArrayList<Permission>();
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("SELECT * FROM permission WHERE userName = ?;");
			statement.setString(1, userName);
			
			result = statement.executeQuery();
			
			while (result.next()) {
				Permission permission = new Permission(result.getInt("permissionID"), result.getString("userName"), result.getInt("fileID"),
					result.getInt("permissionType"));
				accessList.add(permission);
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			accessList = null;
		}
		
		finally {
			connection.close();
			try {
				if (statement != null) {
					statement.close();
				}
				
				if (result != null) {
					result.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return accessList;
	}
	
	public boolean updateAccess(Permission permission) {
		boolean isUpdated;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("UPDATE permission SET permissionType = ? WHERE userName = ? AND fileID = ?;");
			statement.setInt(1, permission.getPermissionType());
			statement.setString(2, permission.getUserName());
			statement.setInt(3, permission.getFileID());
			
			statement.executeUpdate();
			isUpdated = true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			isUpdated = false;
		}
		
		finally {
			connection.close();
			try {
				if (statement != null) {
					statement.close();
				}
				
				if (result != null) {
					result.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return isUpdated;
	}
	
	public boolean delete(Permission permission) {
		boolean isDeleted;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("DELETE FROM permission WHERE userName = ? AND fileID = ?;");
			statement.setString(1, permission.getUserName());
			statement.setInt(2, permission.getFileID());
			
			statement.execute();
			isDeleted = true;
			
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			isDeleted = false;
		}
		
		finally {
			connection.close();
			try {
				if (statement != null) {
					statement.close();
				}
				
				if (result != null) {
					result.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return isDeleted;
	}
	
	// ---------- TEST CASE ---------- //
	public static void main(String[] args) {
		PermissionDAO pd = new PermissionDAO();
		Permission permission = new Permission("mp755", 2, 1);
		Permission permission2 = new Permission("mp755", 3, 0);
		
		/* CREATE PERMISSION */
		/*
		 * pd.create(permission);
		 * pd.create(permission2);
		 */
		
		/* CHECK ACCESS */
		/*
		 * System.out.println(pd.canAccess("mp755", 2));
		 * System.out.println(pd.canAccess("mp755", 5));
		 */
		
		/* CHECK WRITE ACCESS */
		/*
		 * System.out.println(pd.canWrite("mp755", 2));
		 * System.out.println(pd.canWrite("mp755", 3));
		 */
		
		/* GET ACCESS LIST */
		/*
		 * List<Permission> permList = pd.getAccessList("mp755");
		 * 
		 * for(Permission p : permList) {
		 * System.out.printf(
		 * "Username:\t\t%s\nFile ID:\t\t%d\nPermission Type:\t%d\n\n",
		 * p.getUserName(), p.getFileID(), p.getPermissionType());
		 * }
		 */
		
		/* DELETE PERMISSION */
		//pd.delete(permission2);
	}
}
