package fileManagement;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;



/* Write File Handler
 * 		This class is responsible for writing files to a server location.
 * 	
 * Methods:
 * 		+ void writeFile(File file, String userPath)		: Will write all files in the array to a server location. userPath = "[username]//[subfolder]//[subfolder]// ...etc
 * 															: If there are no subfolders, userPath = "[username]".
 * 
 * 		- String getFileName(File f)						: Will eliminate all whitespace in the file name
 * 			-String object containing the file name
 * 	
 * 		- void writeToOutput(ServerConnection c, File f)	: Will transform the file intoa  byte array and will send each byte to the server.
 * 		- void readStream(ServerConnection c)				: Will read the server's response to the file. (Echo statement in php file)
 * 		+ String getResult()								: Will return the result of the file upload.
 * 			-String object containing the server's response. 
 */

public class WriteFileHandler {
	private String result = "";
	private String baseURL = "http://65.185.85.1//scripts//writeFile.php";
	private String fileName = "";
	
	public void writeFile(File file, String userPath, int updateNum) throws IOException {
		ServerConnection c = ServerConnection.getInstance();	
		
		fileName = getFileName(file, updateNum);
			
		c.updateURL(baseURL + "?filename=" + fileName + "&userpath=" + userPath);
		c.setupPostConnection();
			
		writeToOutput(c, file);
		
		readStream(c);
		
		c.closeConnection();
	}
	
	protected String getFileName(File f, int updateNum) {
		fileName = f.getName();                                 // Get the file's name
		fileName = fileName.trim();                             // Eliminate whitespace before and after string
		fileName = fileName.replaceAll("\\s", "");              // Eliminate spaces in file name
                
                if(updateNum >= 0) {
                    fileName += "_" + updateNum;
                }
		
		return fileName;
	}
	
	private void writeToOutput(ServerConnection c, File f) throws IOException {
		OutputStream os = c.getConnection().getOutputStream();
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		long byteLength = f.length();				// Get the length of the file.
		
		for (int i = 0; i < byteLength; i++) {
			os.write(bis.read());					// For each byte that input stream reads, write to output stream. This ensures the entire file is uploaded to the file.
		}
		
		os.close();
		bis.close();
		fis.close();
	}
	
	private void readStream(ServerConnection c) throws IOException {
		// Read the echo statement in the PHP file.
		String s;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(c.getConnection().getInputStream()));
		
		s = null;
		while ((s = in.readLine()) != null) {
			result = s;
		}
		
		in.close();
	}
	
	public String getResult() {
		return result;
	}
	
	// ---------- TEST CASE ---------- //
	public static void main(String[] args) throws IOException {
		String userPath = "test";
		File file = new File("C:\\Users\\mp755\\Documents\\SPRING 2014\\WORK\\CMPSC 221\\QuizCheatSheet2.docx");
		
		WriteFileHandler wfh = new WriteFileHandler();
		
		wfh.writeFile(file, userPath, 0);
		
		System.out.println(wfh.getResult());
	}
}
