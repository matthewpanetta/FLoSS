package fileManagement;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ServerConnection {
	private HttpURLConnection httpUrlConnection;
	private static ServerConnection instance = null;	// Singleton
	
	private ServerConnection() {
		
	}
	
	public static ServerConnection getInstance() {
		if(instance == null) {
			instance = new ServerConnection();
		}
		return instance;
	}
	
	public void updateURL(String url) {
		try {
			httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setupPostConnection() {
		httpUrlConnection.setDoOutput(true);
		try {
			httpUrlConnection.setRequestMethod("POST");	// PHP script uses POST.
		}
		catch (ProtocolException e) {
			e.printStackTrace();
		}
	}
	
	public HttpURLConnection getConnection() {
		return httpUrlConnection;
	}
	
	public void closeConnection() {
		httpUrlConnection.disconnect();
        }
}
