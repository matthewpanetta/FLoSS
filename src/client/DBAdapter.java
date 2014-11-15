package client;

import java.util.ArrayList;
import java.util.List;

import database.DBFacade;

/* Database Adapter
 * 		Client-Side adapter for the database.
 * 		All classes on the client side should communicate with this class, if they intend to communicate with the database.
 * 
 * Methods:
 * 		+ List<File> search(User user, String fileName)		: Will return a list of files that match the fileName searched for. The user argument must be the user who is performing the search.
 * 			-ARRAYLIST of Files with generated fields. 
 */

public class DBAdapter {
	private DBFacade dbf;
	
	public DBAdapter() {
		dbf = DBFacade.getInstance();
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
}
