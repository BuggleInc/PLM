package plm.core.log;

public class DefaultLogger extends LogHandler {

	@Override
	public void log(int type, String message) {
		switch(type) {
		case LogHandler.DEBUG:
			debug(message);
			break;
		case LogHandler.INFO:
			info(message);
			break;
		case LogHandler.ERROR:
			error(message);
			break;
		}
	}

	public void debug(String message) {
		System.out.println("[DEBUG] " + message);
	}

	public void info(String message) {
		System.out.println("[INFO] " + message);
	}

	public void error(String message) {
		System.err.println("[ERROR] " + message);
	}
}
