package plm.core.model;

public abstract class LogHandler {
	
	public static int INFO = 0;
	public static int DEBUG = 1;
	public static int ERROR = 2;
	
	public void log(String message) {
		log(DEBUG, message);
	}
	
	public abstract void log(int type, String message);
}
