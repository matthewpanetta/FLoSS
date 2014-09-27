package testDriver;

import java.io.IOException;

import server.WriteFileHandler;

public class Main {
	
	public static void main(String[] args) throws IOException {
		WriteFileHandler wfh = new WriteFileHandler();
		
		wfh.writeFile();
		
		System.out.println(wfh.getSuccess());
	}
	
}