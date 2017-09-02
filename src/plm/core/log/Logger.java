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

	public static void debug(String message) {
		log(LogHandler.DEBUG, message);
	}
	public static void info(String message) {
		log(LogHandler.INFO, message);
	}
	public static void error(String message) {
		log(LogHandler.ERROR, message);
	}

	public static void log(int type, String message) {
		getInstance().log(type, message);
	}
}
