package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Connection {
	private HttpURLConnection httpUrlConnection;
	
	public Connection(String url) {
		try {
			httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();	// Specify the URL of the writeFile script on the server.
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
	
}
