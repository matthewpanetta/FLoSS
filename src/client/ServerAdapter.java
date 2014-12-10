package client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import database.DBFacade;
import fileManagement.FMFacade;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/* Database Adapter
 * 		Client-Side adapter for the database.
 * 		All classes on the client side should communicate with this class, if they intend to communicate with the database.
 * 
 * Methods:
 * 		+ List<File> search(User user, String fileName)		: Will return a list of files that match the fileName searched for. The user argument must be the user who is performing the search.
 * 			-ARRAYLIST of Files with generated fields. 
 */

public class ServerAdapter {
	private DBFacade dbf;
	private FMFacade fmf;
	private static ServerAdapter instance = null;
	
	private ServerAdapter() {
		dbf = DBFacade.getInstance();
		fmf = FMFacade.getInstance();
	}
	
	public static ServerAdapter getInstance() {
		if(instance == null) {
			instance = new ServerAdapter();
		}
		
		return instance;
	}
	
	public File getFile(String filename, User u){
		return dbf.getFile(u.getUserName(), filename);
        }
        
        public boolean deleteFile(File file){
            boolean deleted = false;
            
            if(fmf.deleteFile(file.getFilePath() + "\\" + file.getFileName()) && dbf.deleteFile(file)) {
                deleted = true;
            }
            
            return deleted;
        }
        
        public boolean renameFile(String userName, File file, String newFilePath) {
            boolean renamed = false;
            int flag = 1;
            
            if(file.getOwner().equals(userName)) {
                flag = 0;
            }
            
            // Get the file extension. If the user did not specify a file extension, add it onto the file name.
            int extensionIndex = file.getFileName().lastIndexOf(".");
            String extension = file.getFileName().substring(extensionIndex);
            if(!newFilePath.endsWith(extension)) {
                newFilePath += extension;
            }
            
            if(dbf.updateFileName(file.getOwner(), file.getFileName(), newFilePath, userName) && fmf.renameFile(file.getFilePath() + "\\" + file.getFileName(), file.getFilePath() + "\\" + newFilePath, flag)) {
                renamed = true;
            }
            
            return renamed;
        }
        
        public boolean recoverFile(File file) {
            boolean recovered = false;
            
            if(dbf.recoverFile(file.getFileID()) && fmf.recoverFile(file.getFilePath() + "\\" + file.getFileName())) {
                Permission perm = new Permission(file.getOwner(), file.getFileID(), 1);
                if(dbf.addPermission(perm)) {
                    recovered = true;
                }
            }
            
            return recovered;
        }
        
        public boolean addFriend(User user, User friend) {
            return dbf.addFriend(user, friend);
        }
        
        public boolean removeFriend(User user, User friend) {
            return dbf.deleteFriend(user, friend);
        }
        
        public List<User> getFriends(User user) {
            return dbf.getFriends(user);
        }
        
        public List<Permission> getPermissionsList(int fileID) {
            return dbf.getCollaboratorList(fileID);
        }
        
        public List<File> getDeletedFileList(String userName) {
            return dbf.getDeletedFileList(userName);
        }
        
        public List<File> getCollaborations(String userName) {
            return dbf.getCollaborations(userName);
        }
        
        public List<File> getAllFiles(String userName) {
            return dbf.getAllFiles(userName);
        }
	
	public void createUser(){
		
	}
	
	public void removeUser(){
		
	}
	
	public void verifyUserCredentials(User u){
		
	}
	
	public boolean addPermission(Permission p){
            return dbf.addPermission(p);
	}
	
	public boolean removePermission(Permission p){
            return dbf.removePermission(p);
	}
        
        public boolean isOwner(String userName, int fileID) {
            return dbf.isOwner(userName, fileID);
        }
	
	public void verifyFile(File f){
		
	}
	
	public void verifyPermissions(User u, File f){
		
	}
	
	public File retrieveFile(String filename){
		return null;
	}
	
	public void addOwner(User u, Permission p) {
		
	}
	
	public void addAccessor(User u, Group g, Permission p) {
		
	}
	
	public boolean checkOwner(User u, Group g){
		return true;
	}
	
	public boolean checkOwner(User u, File f) {
		return true;
	}
	
	public boolean checkAccessor(User u, Group g) {
		return true;
	}
        
        public List<File> getFileList(String userName) {
            return dbf.getAllFiles(userName);
        }
        
        public boolean register(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
            boolean isRegistered;
            
            isRegistered = dbf.registerUser(user);
            
            return isRegistered;
        }
	
        // hackers need to encrypt this later
        public boolean authenticateUser(User u) throws NoSuchAlgorithmException, InvalidKeySpecException{
            return dbf.authenticate(u);
        }
	public List<File> search(User user, String fileName) {
		List<File> fileList = new ArrayList<File>();
		List<File> allFiles = dbf.getAllFiles(user.getUserName());
		
		for(File f : allFiles) {
			if(f.getFileName().equals(fileName)) {
				fileList.add(f);
			}
		}
		
		return fileList;
	}
	
	public void upload(User u, String clientFilePath, String serverFilePath) {
		int index = clientFilePath.lastIndexOf("\\");
		String fileName = clientFilePath.substring(index+1);
		client.File file = new client.File(fileName, serverFilePath, u.getUserName());
		dbf.addFile(file);
		file = dbf.getFile(u.getUserName(), fileName);
		Permission perm = new Permission(u.getUserName(), file.getFileID(), 1);
		
		if(!dbf.canAccess(u.getUserName(), fileName)) {
			dbf.addPermission(perm);
		}
		
		try {
			fmf.upload(clientFilePath, serverFilePath, 0);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reupload(User u, String clientFilePath, String serverFilePath, String fileName) {
		int index = clientFilePath.lastIndexOf("\\");
                int flag = 1;
		client.File file = null;
		
		List<client.File> fileList = dbf.getAllFiles(u.getUserName());
		
		for(client.File f : fileList) {
			if(f.getFileName().equals(fileName)) {
				file = f;
			}
		}
		
		if(!(file==null)) {
			dbf.updateTimestamp(file.getOwner(), fileName);
                        
                        if(file.getOwner().equals(u.getUserName())) {
                               flag = 0;
                        }
                        
                        try {
                                fmf.upload(clientFilePath, serverFilePath, flag);
                        }
                        catch (IOException e) {
                                e.printStackTrace();
                        }
		}
	}
	public User getUser(String username){
            User u = dbf.getUser(username);
            return u;
        }
	public boolean download(User u, String serverFilePath, String clientFilePath) {
		boolean result = false;
		
		int index = serverFilePath.lastIndexOf("/");
		String fileName = serverFilePath.substring(index+1);
		if(dbf.canAccess(u.getUserName(), fileName)) {
			try {
				serverFilePath = serverFilePath.replaceAll("\\s", "");
				fmf.download(serverFilePath, clientFilePath);
				result = true;
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public void retrieve(User user, String fileName) {
		download(user, "temp\\"+user.getUserName()+"\\"+fileName, "C:\\temp\\"+fileName);
		upload(user, "C:\\temp\\"+ fileName, user.getUserName());
	}
	public void updateUser(User u){
            dbf.updateUser(u);
        }
	public static void main(String[] args) {
		User u = new User("mp755", "test123");
		String clientFilePath = "C:\\Users\\mp755\\Documents\\ugates.docx";
		String serverFilePath = "mp755";
		
		ServerAdapter sa = new ServerAdapter();
		//sa.upload(u, clientFilePath, serverFilePath);
		
		serverFilePath = "mp755\\ugates.docx";
		clientFilePath = "C:\\Users\\mp755\\Desktop\\ugates.docx";
		
		//sa.download(u, serverFilePath, clientFilePath);
		
		
		sa.retrieve(u, "ugates.docx");
	}
}
