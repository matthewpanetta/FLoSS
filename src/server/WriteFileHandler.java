package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class WriteFileHandler {
	private String result = "";
	private String baseURL = "http://65.185.85.1//writeFile.php?filename=";
	
	public void writeFile() throws IOException {
		File[] files = getFiles();		
		String fileName = "";	
		ServerConnection c = ServerConnection.getInstance();
		
		for (File f : files) {
			fileName = getFileName(f);
			
			c.updateURL(baseURL + fileName);
			c.setupPostConnection();
			
			writeToOutput(c, f);
			
			readStream(c);
			
			c.closeConnection();
		}
	}
	
	public String getFileName(File f) {
		String fileName = f.getName();				// Get the file's name
		fileName = fileName.trim();					// Eliminate whitespace before and after string
		fileName = fileName.replaceAll("\\s", "");	// Eliminate spaces in file name
		
		return fileName;
	}
	
	public File[] getFiles() {
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
	}
	
	public void writeToOutput(ServerConnection c, File f) throws IOException {
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
	
	public void readStream(ServerConnection c) throws IOException {
		// Read the echo statement in the PHP file.
		String s;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(c.getConnection().getInputStream()));
		
		s = null;
		while ((s = in.readLine()) != null) {
			result = s;
		}
		
		in.close();
	}
	
	
	public String getSuccess() {
		return result;
	}
}
