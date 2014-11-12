package client;

public class DatabaseAdapter {
	public User searchUserDatabase(String username){

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
}
