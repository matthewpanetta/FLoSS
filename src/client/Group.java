package client;

import java.util.ArrayList;

public class Group {
	private String groupName;
	private ArrayList<User> members;
	private String groupAdmin;
	
	public Group(String groupName, ArrayList<User> members, String groupAdmin) {
		super();
		this.groupName = groupName;
		this.members = members;
		this.groupAdmin = groupAdmin;
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public ArrayList<User> getMembers() {
		return members;
	}
	public void setMembers(ArrayList<User> members) {
		this.members = members;
	}
	public String getGroupAdmin() {
		return groupAdmin;
	}
	public void setGroupAdmin(String groupAdmin) {
		this.groupAdmin = groupAdmin;
	}
	
}
