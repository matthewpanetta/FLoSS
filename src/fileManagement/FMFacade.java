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
	
	public void upload(String clientFilePath, String serverFilePath, int updateNum) throws IOException {
                File file = getClientFile(clientFilePath);
		wfh.writeFile(file, serverFilePath, -1);
                wfh.writeFile(file, "temp\\" + serverFilePath, updateNum);
                
                if(updateNum > 3) {
                    int updateToBeDeleted = updateNum - 3;
                    serverFilePath = "temp\\" + serverFilePath;
                    serverFilePath += "\\" + wfh.getFileName(file, updateToBeDeleted);
                    dfh.deleteFile(serverFilePath);
                }
	}
	
	public void download(String serverFilePath, String clientFilePath, int flag) throws MalformedURLException, IOException {
                if(flag >= 0) {
                    serverFilePath += "_" + flag;
                }
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
        
        public boolean renameFile(String oldFilePath, String newFilePath, int updateNum, int flag) {
            boolean renamed = false;
            
            try {
                renamed = rfh.renameFile(oldFilePath, newFilePath, 1);
                
                if(flag == 0) {
                    for(int i = (updateNum - 3); i < updateNum; i++) {
                        rfh.renameFile("temp\\" + oldFilePath + "_" + i, "temp\\" + newFilePath + "_" + i, 1);
                    }
                    
                    rfh.renameFile("temp\\" + oldFilePath + "_" + 0, "temp\\" + newFilePath + "_" + 0, 1);
                }
            } catch(IOException e) {
                Logger.getLogger(FMFacade.class.getName()).log(Level.SEVERE, null, e);
            }
            
            return renamed;
        }
        
        public boolean recoverFile(String filePath) {
            boolean recovered = false;
            
            try {
                recovered = rfh.renameAndCopyFile("temp\\" + filePath + "_0", filePath);
                
            } catch(IOException e) {
                Logger.getLogger(FMFacade.class.getName()).log(Level.SEVERE, null, e);
            }
            
            return recovered;
        }
        
        public boolean rollbackFile(String oldFilePath, String newFilePath) {
            boolean rollback = false;
            
            try {
                rollback = rfh.renameAndCopyFile(oldFilePath, newFilePath);
            } catch(IOException e) {
                Logger.getLogger(FMFacade.class.getName()).log(Level.SEVERE, null, e);
            }
            
            return rollback;
        }
        
        public boolean deleteFileRollbacks(String oldFilePath) {
            boolean deleted = false;
            
            return deleted;
        }
}
