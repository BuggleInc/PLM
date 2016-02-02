package plm.core.log;

public abstract class LogHandler {
	
	public static final int INFO = 0;
	public static final int DEBUG = 1;
	public static final int ERROR = 2;
	
	public void log(String message) {
		log(DEBUG, message);
	}
	
	public abstract void log(int type, String message);
}
