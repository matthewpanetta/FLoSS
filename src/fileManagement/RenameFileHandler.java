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
public class RenameFileHandler {
    private ServerConnection c;
    private String result = "";
    private String baseURL = "http://65.185.85.1//scripts//renameFile.php";
    private String fileName = "";
    
    public boolean renameFile(String oldFilePath, String newFilePath) throws IOException {
        boolean renamed = false;
        
        c = ServerConnection.getInstance();
        c.updateURL(baseURL);

        oldFilePath = oldFilePath.replace(" ", "");
        newFilePath = newFilePath.replace(" ", "");
        
        c.setupPostConnection();
        
        try(OutputStreamWriter out = new OutputStreamWriter(c.getConnection().getOutputStream())) {
            out.write("old=" + oldFilePath + "&new=" + newFilePath);
        }
        
        try(BufferedReader in = new BufferedReader(new InputStreamReader(c.getConnection().getInputStream()))) {
            String curLine;
            String bool = "false";
            
            while((curLine = in.readLine()) != null) {
                bool = curLine;
            }
            
            if(bool.contains("true")) {
                renamed = true;
            }
        }
        
        c.closeConnection();      
        
        return renamed;
    }
    
}
