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
	
	public User searchUserDatabase(String username){
		return null;
	}
	
	public void createUser(){
		
	}
	
	public void removeUser(){
		
	}
	
	public void verifyUserCredentials(User u){
		
	}
	
	public void addPermission(User u, Permission p){
		
	}
	
	public void removePermission(User u, Permission p){
		
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
	
	public void reupload(User u, String clientFilePath, String serverFilePath) {
		int index = clientFilePath.lastIndexOf("\\");
		String fileName = clientFilePath.substring(index+1);
		client.File file = null;
		
		List<client.File> fileList = dbf.getAllFiles(u.getUserName());
		
		for(client.File f : fileList) {
			if(f.getFileName().equals(fileName)) {
				file = f;
			}
		}
		
		if(!(file==null)) {
			dbf.updateTimestamp(file.getOwner(), fileName);
		
			try {
				fmf.upload(clientFilePath, serverFilePath, 1);
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
