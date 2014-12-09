package fileManagement;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FMFacade {
	private GetFileHandler gfh;
	private WriteFileHandler wfh;
        private DeleteFileHandler dfh;
        private RenameFileHandler rfh;
	
	private static FMFacade instance = null;
	
	private FMFacade() {
		gfh = new GetFileHandler();
		wfh = new WriteFileHandler();
                dfh = new DeleteFileHandler();
                rfh = new RenameFileHandler();
	}
	
	public static FMFacade getInstance() {
		if(instance == null) {
			instance = new FMFacade();
		}
		return instance;
	}
	
	public void upload(String clientFilePath, String serverFilePath, int flag) throws IOException {
		File file = getClientFile(clientFilePath);
		wfh.writeFile(file, serverFilePath);
		
		if(flag == 0) {
			wfh.writeFile(file, "temp\\" + serverFilePath);
		}
	}
	
	public void download(String serverFilePath, String clientFilePath) throws MalformedURLException, IOException {
		gfh.getFile(serverFilePath, clientFilePath);
	}
	
	public File getClientFile(String clientFilePath) throws MalformedURLException {
		File tempFile = new File(clientFilePath);
		return tempFile;
	}
        
        public boolean deleteFile(String serverFilePath) {
            boolean deleted = false;
            try {
                deleted = dfh.deleteFile(serverFilePath);
            } catch (IOException ex) {
                Logger.getLogger(FMFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return deleted;
        }
        
        public boolean renameFile(String oldFilePath, String newFilePath) {
            boolean renamed = false;
            
            try {
                renamed = rfh.renameFile(oldFilePath, newFilePath);
            } catch(IOException e) {
                Logger.getLogger(FMFacade.class.getName()).log(Level.SEVERE, null, e);
            }
            
            return renamed;
        }
}
