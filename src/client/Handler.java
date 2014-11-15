package client;

public abstract class Handler {
	private DBAdapter dbAdapter;
	
	public DBAdapter getDatabaseAdapter(){
		return dbAdapter;
	}
}
