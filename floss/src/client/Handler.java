package client;

public abstract class Handler {
	private DatabaseAdapter dbAdapter;
	
	public DatabaseAdapter getDatabaseAdapter(){
		return dbAdapter;
	}
}
