package fileManagement;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import client.User;

/* Write File Handler
 * 		This class is responsible for writing files to a server location.
 * 	
 * Methods:
 * 		+ void writeFile(File[] files)						: Will write all files in the array to a server location
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
	
	public void writeFile(File[] files, String userName) throws IOException {
		//File[] files = getFiles();
		ServerConnection c = ServerConnection.getInstance();	
		
		for (File f : files) {
			fileName = getFileName(f);
			
			c.updateURL(baseURL + "?filename=" + fileName + "&username=" + userName);
			c.setupPostConnection();
			
			writeToOutput(c, f);
			
			readStream(c);
			
			c.closeConnection();
		}
	}
	
	private String getFileName(File f) {
		fileName = f.getName();						// Get the file's name
		fileName = fileName.trim();					// Eliminate whitespace before and after string
		fileName = fileName.replaceAll("\\s", "");	// Eliminate spaces in file name
		
		return fileName;
	}
	
	/*public File[] getFiles() {
		// Opens a file chooser dialog GUI where the user selects which file(s) they would like to upload.
		JFileChooser chooser = new JFileChooser();
	
		// Restrict the user to certain file formats
		FileNameExtensionFilter filter = new FileNameExtensionFilter("All Acceptable Files", "doc", "docx", "xlsx", "pptx", "txt", "png", "jpg",
			"gif");
		
		chooser.setFileFilter(filter);
		
		// Allow the user to upload multiple files.
		chooser.setMultiSelectionEnabled(true);
		
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFiles();
		}
		
		return null;
	}*/
	
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
		//String userName = "test";		
		
		WriteFileHandler wfh = new WriteFileHandler();
		
		//wfh.writeFile(userName);
		
		System.out.println(wfh.getResult());
	}
}
