package plm.core.log;

public class Logger {

	public static LogHandler logger;

	public static  LogHandler getInstance() {
		if(logger == null) {
			logger = new DefaultLogger();
		}
		return logger;
	}

	public static void setLogger(LogHandler l) {
		logger = l;
	}

	public static void log(String message) {
		log(LogHandler.DEBUG, message);
	}

	public static void log(int type, String message) {
		getInstance().log(type, message);
	}
}
