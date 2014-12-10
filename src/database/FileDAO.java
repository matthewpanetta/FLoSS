package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import client.File;

/* FileDAO class
 * 		This class is responsible for creating, reading, updating, and deleting file entires in the database.
 * 		*NOTE* This class is NOT responsible for managing the actual file on the server. It is strictly for the database.
 * 
 * Methods:
 * 		+ boolean create(File file)							: Will create a file entry on the database. Date uploaded and Date modified are automatically generated on the database end upon insertion.
 * 			-TRUE if file entry created, FALSE if not created.
 * 
 * 		+ File getFile(String userName, String fileName)	: Will retrieve a file entry on the database. The username and filename together form a composite key, which will get a unique file entry.
 * 			-FILE object with everything generated.
 * 
 * 		+ File getFile(int fileID)							: Will do the same as above
 *
 *              + boolean isOwner(String userName, int fileID)                  : Checks if a user is the owner of a file.
 *                      -TRUE if userName is owner, FALSE if not owner.
 *
 * 		+ List<File> getFileList(String userName)			: Will retrieve every file entry for one specified user.
 * 			-ARRAYLIST of File objects, with everything generated.
 * 
 * 		+ boolean updateTimestamp(String userName, String fileName)
 * 															: Will update the dateLastModified timestamp on the specified file entry on the database.
 * 			-TRUE if timestamp updated, FALSE if not updated.
 * 
 * 		+ boolean updateFileName(String userName, String oldFileName, String newFileName)
 * 															: Will modify the file name of a specified file entry on the database.
 * 			-TRUE if fileName updated, FALSE if fileName not updated.
 * 
 * 		+ boolean delete(String userName, String fileName)	: Will remove a file entry from the database.
 * 			-TRUE if file entry deleted, FALSE if file entry still exists. 
 */

public class FileDAO {
	private DBConnection connection = DBConnection.getInstance();
	private PreparedStatement statement = null;
	private ResultSet result = null;
	
	public boolean create(File file) {
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("INSERT INTO file (owner, fileName, filePath) VALUES(?,?,?);");
			
			statement.setString(1, file.getOwner());
			statement.setString(2, file.getFileName());
			statement.setString(3, file.getFilePath());
			
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
	
	public File getFile(String userName, String fileName) {
		File file;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("SELECT * FROM file WHERE owner = ? AND fileName = ?;");
			statement.setString(1, userName);
			statement.setString(2, fileName);
			
			result = statement.executeQuery();
			
			
			if(!result.isBeforeFirst()) {
				file = null;
			}
			
			else {
				result.next();
				
				Date modifiedDate = result.getTimestamp("dateLastModified");
				Date uploadDate = result.getTimestamp("dateUploaded");
				
				file = new File(result.getInt("fileID"), result.getString("fileName"), result.getString("filePath"), result.getString("owner"), modifiedDate, uploadDate);				
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			file = null;
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
		
		return file;
	}
	
	public File getFile(int fileID) {
		File file;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("SELECT * FROM file WHERE fileID = ?;");
			statement.setInt(1, fileID);
			
			result = statement.executeQuery();
			
			
			if(!result.isBeforeFirst()) {
				file = null;
			}
			
			else {
				result.next();
				
				Date modifiedDate = result.getTimestamp("dateLastModified");
				Date uploadDate = result.getTimestamp("dateUploaded");
				
				file = new File(result.getInt("fileID"), result.getString("fileName"), result.getString("filePath"), result.getString("owner"), modifiedDate, uploadDate);				
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			file = null;
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
		
		return file;
	}
        
        public boolean isOwner(String userName, int fileID) {
            boolean isOwner = false;
            
            try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("SELECT owner FROM file WHERE fileID = ?;");
			statement.setInt(1, fileID);
			result = statement.executeQuery();
                        
                        result.next();
			
			String owner = result.getString("owner");
                        
                        if(owner.equals(userName)) {
                            isOwner = true;
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
		
		return isOwner;
        }
	
	public List<File> getFileList(String userName) {
		List<File> fileList = new ArrayList<File>();
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("SELECT * FROM file WHERE owner = ?;");
			statement.setString(1, userName);
			
			result = statement.executeQuery();
			
			while(result.next()) {
				
				Date modifiedDate = result.getTimestamp("dateLastModified");
				Date uploadDate = result.getTimestamp("dateUploaded");
				
				File file = new File(result.getInt("fileID"), result.getString("fileName"), result.getString("filePath"), result.getString("owner"), modifiedDate, uploadDate);
				fileList.add(file);
			}			
		}
		
		catch (SQLException e) {
			fileList = null;
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
		
		return fileList;
	}
	
	public boolean updateTimestamp(String userName, String fileName) {
		boolean isUpdated;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("UPDATE file SET fileName = ? WHERE owner = ? AND fileName = ?;");
			statement.setString(1, fileName + "u");
			statement.setString(2, userName);
			statement.setString(3, fileName);
			
			statement.executeUpdate();
			
			statement = connection.getConnection().prepareStatement("UPDATE file SET fileName = ? WHERE owner = ? AND fileName = ?;");
			statement.setString(1, fileName);
			statement.setString(2, userName);
			statement.setString(3, fileName + "u");
			
			statement.executeUpdate();
			isUpdated = true;
		} 
		catch (SQLException e) {
			isUpdated = false;
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
		
		return isUpdated;
	}
	
	public boolean updateFileName(String userName, String oldFileName, String newFileName) {
		boolean isUpdated;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("UPDATE file SET fileName = ? WHERE owner = ? AND fileName = ?;");
			statement.setString(1, newFileName);
			statement.setString(2, userName);
			statement.setString(3, oldFileName);
			
			statement.executeUpdate();
			isUpdated = true;
		} 
		catch (SQLException e) {
			isUpdated = false;
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
		
		return isUpdated;
		
	}

	public boolean delete(String userName, String fileName) {
		boolean isDeleted;
		
		try {
			connection.connect();
			statement = connection.getConnection().prepareStatement("DELETE FROM file WHERE owner = ? AND fileName = ?;");
			statement.setString(1, userName);
			statement.setString(2, fileName);
			
			statement.execute();
			isDeleted = true;
		} 
		
		catch (SQLException e) {
			isDeleted = false;
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
		
		return isDeleted;
		}
	
	// ---------- TEST CASE ---------- //
	public static void main(String[] args) {
		FileDAO fd = new FileDAO();
		File file = new File("LOL.txt", "C:\\Users\\mp755\\LOL.txt", "mp755");
		File file2 = new File("HAHA.txt", "C:\\Users\\mp755\\HAHA.txt", "John");
		File file3 = new File("LMAO.txt", "C:\\Users\\mp755\\LMAO.txt", "mp755");
		
		// COMMENT OUT LINES THAT YOU DO NOT WANT TO TEST! //
		
		/* CREATE FILE */		
		/*fd.create(file);
		fd.create(file2);
		fd.create(file3);
		*/
		
		/* GET FILE */		
		/*File getTest = fd.getFile("mp755", "LOL.txt");
		System.out.printf("FileName:\t\t%s\nOwner:\t\t\t%s\nDate Last Modified:\t%s\nDate Uploaded:\t\t%s", getTest.getFileName(), getTest.getOwner(), getTest.getModifiedDate(), getTest.getCreationDate());
		
		getTest = fd.getFile("John", "HAHA.txt");
		System.out.printf("\n\nFileName:\t\t%s\nOwner:\t\t\t%s\nDate Last Modified:\t%s\nDate Uploaded:\t\t%s", getTest.getFileName(), getTest.getOwner(), getTest.getModifiedDate(), getTest.getCreationDate());
		*/
		
		/* GET FILE LIST */		
		/*List<File> fileList = fd.getFileList("mp755");
		
		for(File f : fileList) {
			System.out.printf("FileName:\t\t%s\nOwner:\t\t\t%s\nDate Last Modified:\t%s\nDate Uploaded:\t\t%s\n\n", f.getFileName(), f.getOwner(), f.getModifiedDate(), f.getCreationDate());
		}
		*/
		
		/* UPDATE TIMESTAMP */
		/*fd.updateTimestamp("mp755", "LOL.txt");
		File testFile = fd.getFile("mp755", "LOL.txt");
		
		System.out.printf("FileName:\t\t%s\nOwner:\t\t\t%s\nDate Last Modified:\t%s\nDate Uploaded:\t\t%s", testFile.getFileName(), testFile.getOwner(), testFile.getModifiedDate(), testFile.getCreationDate());
		*/
		
		/* UPDATE FILENAME */		
		/*fd.updateFileName("mp755", "LOL.txt", "ROFL.txt");
		File testFile = fd.getFile("mp755", "ROFL.txt");
		
		System.out.printf("FileName:\t\t%s\nOwner:\t\t\t%s\nDate Last Modified:\t%s\nDate Uploaded:\t\t%s", testFile.getFileName(), testFile.getOwner(), testFile.getModifiedDate(), testFile.getCreationDate());
		
		fd.updateFileName("mp755", "ROFL.txt", "LOL.txt");
		*/
		
		/* DELETE FILE */
		/*fd.delete("mp755", "LMAO.txt");
		
		List<File> fileList = fd.getFileList("mp755");
		
		for(File f : fileList) {
			System.out.printf("FileName:\t\t%s\nOwner:\t\t\t%s\nDate Last Modified:\t%s\nDate Uploaded:\t\t%s", f.getFileName(), f.getOwner(), f.getModifiedDate(), f.getCreationDate());
		}
		
		fd.create(file3);
		*/
	}
}
