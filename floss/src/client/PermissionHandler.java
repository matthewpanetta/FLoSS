package client;

public class PermissionHandler extends Handler{
	private Permission permission;
	
	public void addPermission(User owner, User friend, String fileName, String type){
		// Give a user read permission to a file.
		permission = new Permission('r');
		super.getDatabaseAdapter().addPermission(owner, permission);
		
	}
	public void removePermission(User owner, User friend, String fileName, String type){
		// Remove the read permission from the file for a specific user.
		permission = new Permission('d');
		super.getDatabaseAdapter().removePermission(owner, permission);
		
	}
	public void addOwner(User owner){
		// Add an owner to a file.
		permission = new Permission('o');
		super.getDatabaseAdapter().addOwner(owner, permission);
		
	}
	public void addAccessorToGroup(User friend, Group g){
		// Add a user to the group
		permission = new Permission('a');
		super.getDatabaseAdapter().addAccessor(friend, g, permission);
		
	}
	public boolean checkOwner(User owner, Group g){
		return super.getDatabaseAdapter().checkOwner(owner, g);
	}
	public boolean checkOwner(User owner, File f) {
		return super.getDatabaseAdapter().checkOwner(owner, f);
	}
	public boolean checkAccessor(User friend, Group g){
		return super.getDatabaseAdapter().checkAccessor(friend, g);
	}
}
