package fileManagement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/* Get File Handler
 * 		This class is responsible for getting a requested file from the server, and saving it to a client's PC.
 * 
 * Methods:
 * 		+ void getFile(String userPath, String clientPath)	: Will get the file from the userPath and save it to the clientPath.
 * 															: userPath must have the user's username as well as the user's intended file. Ex: test//document.docx
 * 															: clientPath must have the full filepath (including filename) to where the client wishes to save the file. 
 * 															: clientPath Example: C://Users//test//Desktop//document.docx
 */

public class GetFileHandler {
	private String baseURL = "http://65.185.85.1//users//";
	
	
	public void getFile(String userPath, String clientPath) throws MalformedURLException, IOException {
         URLConnection http = new URL(baseURL + userPath).openConnection();

         // Start copying!
         InputStream in = http.getInputStream();
         try {
              OutputStream out = new FileOutputStream(clientPath);
              try {
                   byte[] buf = new byte[512];
                   int read;

                   while ((read = in.read(buf)) > 0)
                        out.write(buf, 0, read);
              } finally {
                   out.close();
              }
         } finally {
              in.close();
         }
    }
	
	// ---------- TEST CASE ---------- //
	public static void main(String[] args) throws MalformedURLException, IOException {
		GetFileHandler gfh = new GetFileHandler();
		gfh.getFile("test//QuizCheatSheet2.docx", "C://Users//mp755//Desktop//test.docx");
	}
}
