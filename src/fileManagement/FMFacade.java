package fileManagement;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class FMFacade {
	private GetFileHandler gfh;
	private WriteFileHandler wfh;
	
	private static FMFacade instance = null;
	
	private FMFacade() {
		gfh = new GetFileHandler();
		wfh = new WriteFileHandler();
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
            System.out.println("Hello");
		gfh.getFile(serverFilePath, clientFilePath);
	}
	
	public File getClientFile(String clientFilePath) throws MalformedURLException {
		File tempFile = new File(clientFilePath);
		return tempFile;
	}
}
