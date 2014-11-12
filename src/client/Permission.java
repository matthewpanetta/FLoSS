package client;

public class Permission {
	private User ownerUser;
	private User permittedUser;
	private String fileName;
	private char permissionType;
	
	// Permission Type Legend
	// r = read
	// w = write
	// m = modify
	// d = delete
	// o = owner
	
	public Permission(char permissionFlag) {
		permissionType = permissionFlag;
	}

	public User getOwnerUser() {
		return ownerUser;
	}

	public void setOwnerUser(User ownerUser) {
		this.ownerUser = ownerUser;
	}

	public User getPermittedUser() {
		return permittedUser;
	}

	public void setPermittedUser(User permittedUser) {
		this.permittedUser = permittedUser;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public char getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(char permissionType) {
		this.permissionType = permissionType;
	}
}
