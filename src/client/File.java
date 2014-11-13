package client;

import java.util.Date;

public class File {
	private String fileName;
	private String filePath;
	private String owner;
	private Date modifiedDate;
	private Date creationDate;
	
	public File(String fileName, String filePath, String userName, Date modifiedDate, Date creationDate) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.owner = userName;
		this.modifiedDate = modifiedDate;
		this.creationDate = creationDate;
	}
	
	public File(String fileName, String filePath, String userName) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.owner = userName;
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
	public String getOwner() {
		return owner;
	}
	public void setOwner(String userName) {
		this.owner = userName;
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
