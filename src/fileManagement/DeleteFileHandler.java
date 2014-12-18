/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fileManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 *
 * @author mp755
 */
public class DeleteFileHandler {
    private String result = "";
    private String baseURL = "http://65.185.85.1//scripts//deleteFile.php";
    private String fileName = "";
    private ServerConnection c;
    
    public DeleteFileHandler() {
        
    }
    
    public boolean deleteFile(String filePath) throws IOException {
        c = ServerConnection.getInstance();
        c.updateURL(baseURL);
        
        boolean deleted = false;
        filePath = filePath.replace(" ", "");
        
        c.setupPostConnection();
        
        try(OutputStreamWriter out = new OutputStreamWriter(c.getConnection().getOutputStream())) {
            out.write("filepath=" + filePath);
        }
        
        try(BufferedReader in = new BufferedReader(new InputStreamReader(c.getConnection().getInputStream()))) {
            String curLine;
            String bool = "false";
            
            while((curLine = in.readLine()) != null) {
                bool = curLine;
            }
            
            if(bool.contains("true")) {
                deleted = true;
            }
        }
        
        c.closeConnection();
        
        return deleted;
    }
    
    public boolean deleteDirectory(String dirPath) throws IOException {
        boolean isDeleted = false;
        baseURL = "http://65.185.85.1//scripts//deleteDirectory.php";
        
        c = ServerConnection.getInstance();
        c.updateURL(baseURL);
        
        boolean deleted = false;
        c.setupPostConnection();
        
        try(OutputStreamWriter out = new OutputStreamWriter(c.getConnection().getOutputStream())) {
            out.write("filepath=" + dirPath);
            isDeleted = true;
        }
        
        return isDeleted;
    }
}
