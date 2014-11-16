package client;

public abstract class Handler {
	private ServerAdapter dbAdapter;
	
	public ServerAdapter getDatabaseAdapter(){
		return dbAdapter;
	}
}
