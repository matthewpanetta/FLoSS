package client;

public class Permission {
	private String userName;
	private int permissionID;
	private int fileID;
	private int permissionType;		// 0 = Read Access, 1 = Write Access
	
	public Permission(String userName, int fileID, int permissionType) {
		this.userName = userName;
		this.fileID = fileID;
		this.permissionType = permissionType;
	}
	
	public Permission(int permissionID, String userName, int fileID, int permissionType) {
		this.permissionID = permissionID;
		this.userName = userName;
		this.fileID = fileID;
		this.permissionType = permissionType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getFileID() {
		return fileID;
	}

	public void setFileID(int fileID) {
		this.fileID = fileID;
	}

	public int getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(int permissionType) {
		this.permissionType = permissionType;
	}

	public int getPermissionID() {
		return permissionID;
	}

	public void setPermissionID(int permissionID) {
		this.permissionID = permissionID;
	}
	
	
}
