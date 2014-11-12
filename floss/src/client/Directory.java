package client;

import java.util.ArrayList;

public class Directory {
	private String dirName;
	private String dirPath;
	private ArrayList<File> storedFiles;
	private ArrayList<Directory> storedDir;
	
	public Directory(String dirName, String dirPath, ArrayList<File> storedFiles, ArrayList<Directory> storedDir) {
		super();
		this.dirName = dirName;
		this.dirPath = dirPath;
		this.storedFiles = storedFiles;
		this.storedDir = storedDir;
	}
	
	public String getDirName() {
		return dirName;
	}
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}
	public String getDirPath() {
		return dirPath;
	}
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}
	public ArrayList<File> getStoredFiles() {
		return storedFiles;
	}
	public void setStoredFiles(ArrayList<File> storedFiles) {
		this.storedFiles = storedFiles;
	}
	public ArrayList<Directory> getStoredDir() {
		return storedDir;
	}
	public void setStoredDir(ArrayList<Directory> storedDir) {
		this.storedDir = storedDir;
	}
	
}
