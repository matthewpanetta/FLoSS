package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private String	driverAddress;							
	private String	dbAddress;
	private String	dbUserName;		
	private String	dbPassword;		
	
	private Connection con;
	
	public static DBConnection instance = null;		// Singleton
	
	
	private DBConnection(){
		driverAddress = "com.mysql.jdbc.Driver";
		dbAddress = "jdbc:mysql://65.185.85.1:3306/floss";		// The address of the database:jdbc:RDBMS://IPaddress:port/title of database
		dbUserName = "mp755";
		dbPassword = "Qpalzm01";
	} // END CONNECTION HANDLER
	
	public static DBConnection getInstance() {
		if(instance == null) {
			instance = new DBConnection();
		}
		
		return instance;
	}
	
	public void connect(){
		try {
			Class.forName(driverAddress);
		}
		catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		// Creating a variable for the connection called "con"
		
		try {
			con = DriverManager.getConnection(dbAddress, dbUserName, dbPassword);
		} 
                catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e) {
                        e.printStackTrace();
                }
		catch (SQLException e) {
			e.printStackTrace();
		}
	} // END CONNECT
	
	public Connection getConnection() {
		return con;
	}

	public void close() {
		try {
			con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
