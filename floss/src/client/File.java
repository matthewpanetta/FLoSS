package client;

import java.util.Date;

public class File {
	private String fileName;
	private String filePath;
	private User owner;
	private Date modifiedDate;
	private Date creationDate;
	
	public File(String fileName, String filePath, User owner, Date modifiedDate, Date creationDate) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.owner = owner;
		this.modifiedDate = modifiedDate;
		this.creationDate = creationDate;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
}
