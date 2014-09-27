package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class WriteFileHandler {
	String result = "";
	
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
	
	public void writeFile() throws IOException {
		File[] files = getFiles();
		
		String fileName = "";
		String s = "";
		
		HttpURLConnection httpUrlConnection;
		
		OutputStream os;
		FileInputStream fis;
		BufferedInputStream bis;
		
		long byteLength;
		
		for (File f : files) {
			fileName = f.getName();		// Get the file's name
			fileName.trim();			// Eliminate whitespace before and after string
			fileName = fileName.replaceAll("\\s", "");	// Eliminate spaces in file name
			
			httpUrlConnection = (HttpURLConnection) new URL("http://65.185.85.1//writeFile.php?filename=" + fileName).openConnection();	// Specify the URL of the writeFile script on the server.
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setRequestMethod("POST");	// PHP script uses POST.
			
			os = httpUrlConnection.getOutputStream();
			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis);
			
			byteLength = f.length();	// Get the length of the file.
			
			for (int i = 0; i < byteLength; i++) {
				os.write(bis.read());	// For each byte that input stream reads, write to output stream. This ensures the entire file is uploaded to the file.
			}
			
			os.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
			
			s = null;
			while ((s = in.readLine()) != null) {
				result = s;
			}
			
			in.close();
			fis.close();
		}
	}
	
	public String getSuccess() {
		return result;
	}
}
