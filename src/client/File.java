package client;

import java.util.Date;

public class File {
	private int     fileID;
        private int     updateNum;
	private String	fileName;
	private String	filePath;
	private String	owner;
        private String  lastModifier;
	private Date	modifiedDate;
	private Date	creationDate;
        private FileLog fileLog;
	
	public File(int fileID, int updateNum, String fileName, String filePath, String userName, String lastModifier, Date modifiedDate, Date creationDate) {
		super();
		this.fileID = fileID;
                this.updateNum = updateNum;
		this.fileName = fileName;
		this.filePath = filePath;
		this.owner = userName;
                this.lastModifier = lastModifier;
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
	
	public int getFileID() {
		return fileID;
	}
	
	public void setFileID(int fileID) {
		this.fileID = fileID;
	}
        
        public int getUpdateNum() {
                return updateNum;
        }
        
        public void setUpdateNum(int updateNum) {
                this.updateNum = updateNum;
        }
        
        public String getLastModifier() {
                return lastModifier;
        }
        
        public void setLastModifier(String lastModifier) {
                this.lastModifier = lastModifier;
        }
        
        public void setFileLog(FileLog fileLog) {
            this.fileLog = fileLog;
        }
        
        public FileLog getFileLog() {
            return fileLog;
        }
	
}
