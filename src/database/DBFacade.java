package database;

import java.util.ArrayList;
import java.util.List;

import client.File;
import client.Permission;
import client.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
/* Database Facade class
 * 		This class wraps all functionality in the database package in one class.
 * 		Any class outside of the database package should associate only with this class.
 * 		This class is a singleton.
 * 
 * Methods:
 * 		This class contains many of the same methods as other classes in this package. Check those respective classes for info on those methods.
 * 		
 * 		+ boolean registerUser(User user)					: Will register a user and log them on.
 * 		+ List<File> getCollaborations(String userName)		: Will get all files that a user can access from other users. (Files in which the user is granted read/write access, but does not own). 
 * 		+ List<File> getAllFiles(String userName)			: Will get all files that a user owns and has access to.
 */

public class DBFacade {
	private FileDAO fid;
	private FriendDAO frd;
	private PermissionDAO pd;
	private UserDAO ud;
	
	public static DBFacade instance = null;
	
	private DBFacade() {
		fid = new FileDAO();
		frd = new FriendDAO();
		pd = new PermissionDAO();
		ud = new UserDAO();
	}
	
	public static DBFacade getInstance() {
		if(instance == null) {
			instance = new DBFacade();
		}
		
		return instance;
	}
	
	/* USER DAO */
	public boolean registerUser(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
		boolean isRegistered = ud.create(user);
		
		if(isRegistered) {
			ud.authenticate(user);
		}
		
		return isRegistered;
	}
	
	public boolean authenticate(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
		boolean authenticated = ud.authenticate(user);
		
		return authenticated;
	}
	
	public boolean deauthenticate(User user) {
		boolean deauthenticated = ud.deauthenticate(user);
		
		return deauthenticated;
	}
	
	public boolean isOnline(String userName) {
		boolean isOnline = ud.isOnline(userName);
		
		return isOnline;
	}
	
	public User getUser(String userName) {
		User user = ud.getUser(userName);
		
		return user;
	}
	
	public List<User> getAllUsers() {
		List<User> userList = ud.getAllUsers();
		
		return userList;
	}
	
	public boolean updateUser(User user) {
		boolean userChanged = false;
            try {
                userChanged = ud.update(user);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(DBFacade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(DBFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
		
		return userChanged;
	}
	
	public boolean removeUser(User user) {
		boolean isRemoved = ud.delete(user.getUserName());
		
		return isRemoved;
	}
        
        public boolean updateUserPassword(String userName, String password) {
                boolean isUpdated = ud.updateUserPassword(userName, password);
                
                return isUpdated;
        }
	
	/* FRIEND DAO */
	public List<User> getFriends(User user) {
		List<User> friendList = frd.getAllFriends(user);
		
		return friendList;
	}
	
	public boolean addFriend(User user, User friend) {
		boolean areFriends = frd.create(user, friend);
		
		return areFriends;
	}
	
	public boolean deleteFriend(User user, User friend) {
		boolean deleted = frd.delete(user, friend);
		
		return deleted;
	}
	
	public boolean checkFriends(User user, User friend) {
		boolean areFriends = frd.isFriendsWith(user, friend);
		
		return areFriends;
	}
	
	/* FILE DAO */
	public boolean addFile(File file) {
		File test = fid.getFile(file.getOwner(), file.getFileName());
		boolean created;
		
		if(test == null) {
			created = fid.create(file);
		} else {
			fid.updateTimestamp(file.getOwner(), file.getFileName(), file.getOwner(), file.getUpdateNum()-1);
			created = false;
		}
		
		return created;
	}
        
        public boolean addFile(File file, String modifierName) {
		File test = fid.getFile(file.getOwner(), file.getFileName());
		boolean created;
		
		if(test == null) {
			created = fid.create(file);
		} else {
			fid.updateTimestamp(file.getOwner(), file.getFileName(), modifierName, file.getUpdateNum()-1);
			created = false;
		}
		
		return created;
	}
	
	public boolean deleteFile(File file) {
		boolean deleted = fid.delete(file.getOwner(), file.getFileName());
		
		return deleted;
	}
	
	public List<File> getFiles(String userName) {
		List<File> fileList = fid.getFileList(userName);
		
		return fileList;
	}
        
        public List<File> getDeletedFileList(String userName) {
                List<File> fileList = fid.getDeletedFileList(userName);
		
		return fileList;
        }
	
	public File getFile(int fileID) {
		File file = fid.getFile(fileID);
		
		return file;
	}
	
	public File getFile(String userName, String fileName) {
		File file = fid.getFile(userName, fileName);
		
		return file;
	}
        
        public boolean isOwner(String userName, int fileID) {
                boolean isOwner = fid.isOwner(userName, fileID);
                
                return isOwner;
        }
        
        public boolean recoverFile(int fileID) {
                boolean recovered = fid.recoverFile(fileID);
                
                return recovered;
        }
        
        public boolean rollbackFile(int fileID, int updateNum) {
                boolean rollback = fid.rollbackFile(fileID, updateNum);
                
                return rollback;
        }
	
	public boolean updateTimestamp(String userName, String fileName, String modifierName, int updateNum) {
		boolean isUpdated = fid.updateTimestamp(userName, fileName, modifierName, updateNum);
		
		return isUpdated;
	}
	
	public boolean updateFileName(String ownerName, String oldName, String newName, String modifierName) {
		boolean isChanged = fid.updateFileName(ownerName, oldName, newName, modifierName);
		
		return isChanged;
	}
	
	/* PERMISSION DAO */
	public boolean addPermission(Permission permission) {
		boolean created = pd.create(permission);
		
		return created;
	}
	
	public boolean removePermission(Permission permission) {
		boolean removed = pd.delete(permission);
		
		return removed;
	}
	
	public boolean canAccess(String userName, File file) {
		boolean access = pd.canAccess(userName, file.getFileID());
		
		return access;		
	}
	
	public boolean canAccess(String userName, String fileName) {
		boolean access = false;
		List<File> tempFileList = getAllFiles(userName);
                File tempFile = null;
                
                for(File f : tempFileList) {
                    if(f.getFileName().equals(fileName)) {
                        tempFile = f;
                        break;
                    }
                }
		
		if(!(tempFile == null)) {
			access = pd.canAccess(userName, tempFile.getFileID());
		}
		
		return access;
	}
	
	public boolean canWrite(String userName, File file) {
		boolean write = pd.canWrite(userName, file.getFileID());
		
		return write;
	}
	
	public boolean canWrite(String userName, String fileName) {
		File tempFile = fid.getFile(userName, fileName);
		
		boolean write = pd.canWrite(userName, tempFile.getFileID());
		return write;
	}
	
	public boolean updateAccess(Permission permission) {
		boolean updated = pd.updateAccess(permission);
		
		return updated;
	}
	
	public List<Permission> getAccessList(String userName) {
		List<Permission> accessList = pd.getAccessList(userName);
		
		return accessList;
	}
        
        public List<Permission> getCollaboratorList(int fileID) {
                List<Permission> collabList = pd.getCollaboratorList(fileID);
                
                return collabList;
        }
	
	public List<File> getCollaborations(String userName) {
		List<Permission> permList = getAccessList(userName);
		List<File> fileList = new ArrayList<File>();
		
		for(Permission p : permList) {
			File f = fid.getFile(p.getFileID());
			
			if(!(f.getOwner().equals(userName))) {
				fileList.add(f);	
			}
		}
		
		return fileList;
	}
	
	public List<File> getAllFiles(String userName) {
		List<File> collabList = getCollaborations(userName);
		List<File> ownedList = getFiles(userName);
		
		List<File> masterList = new ArrayList<File>();
		
		masterList.addAll(collabList);
		masterList.addAll(ownedList);
		
		return masterList;
	}
}
