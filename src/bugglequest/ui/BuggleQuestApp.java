package bugglequest.ui;


public class BuggleQuestApp {

	public static void main(String args[]) {
		
		if (System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "BuggleQuest");
		}
		
		MainFrame.getInstance().setVisible(true);
	}

	/*
	 * static void presentException(Throwable t) { String title =
	 * "Unable to run the " + BuggleApp.class.getName() + " application.";
	 * String message = title + " \n" +
	 * "This is certainly because the app is in very early stage of development. \n"
	 * + "Please contact the authors.\n" + "Details:" + t.toString() + "\n"; for
	 * (StackTraceElement s : t.getStackTrace()) { if
	 * (s.getClassName().contains("bugglequest.BugglePanel")) break; message +=
	 * "  in " + s.getClassName() + "." + s.getMethodName() + " at " +
	 * s.getFileName() + ":" + s.getLineNumber() + "\n"; } Throwable t2 =
	 * t.getCause(); if (t2 != null) { message += "Caused by:\n  " +
	 * t2.toString() + "\n"; for (StackTraceElement s : t.getStackTrace()) { if
	 * (s.getClassName().contains("bugglequest.BugglePanel")) break; message +=
	 * "    in " + s.getClassName() + "." + s.getMethodName() + " at " +
	 * s.getFileName() + ":" + s.getLineNumber() + "\n"; } }
	 * JOptionPane.showMessageDialog(null, message, title,
	 * JOptionPane.ERROR_MESSAGE); }
	 */
}
