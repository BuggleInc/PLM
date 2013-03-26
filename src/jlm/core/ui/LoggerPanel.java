package jlm.core.ui;

import javax.swing.JTextArea;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import jlm.core.model.Game;
import jlm.core.model.LogWriter;


public class LoggerPanel extends JTextArea implements LogWriter {

	private static final long serialVersionUID = 468774822833769775L;

	public LoggerPanel(Game game) {
		super();
		setEditable(false);
		setToolTipText("Where error and other messages get written");
		game.setOutputWriter(this);
	}
	
	public void clear() {
		setText(null);
	}
	
	@Override
	public void log(String msg) {
		append(msg);
	}

	@Override
	public void log(DiagnosticCollector<JavaFileObject> diagnostics) {
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {			
			append(diagnostic.getSource().getName()+":"+diagnostic.getLineNumber()+":"+ diagnostic.getMessage(null));
			append("\n");
		}
	}

	/**
	 * Add an exception into the text area
	 * 
	 * @param e
	 *            what to log
	 */
	public void log(Exception e) {
		append(e.toString() + "\n");
		for (StackTraceElement s : e.getStackTrace()) {
			if (s.getClassName().contains("bugglequest.BugglePanel"))
				break;
			append("  in " + s.getClassName() + "." + s.getMethodName() + " at " + s.getFileName() + ":"
					+ s.getLineNumber() + "\n");
		}
		Throwable t = e.getCause();
		if (t != null) {
			append("Caused by:\n  " + t.toString() + "\n");
			for (StackTraceElement s : t.getStackTrace()) {
				if (s.getClassName().contains("bugglequest.BugglePanel"))
					break;
				append("    in " + s.getClassName() + "." + s.getMethodName() + " at " + s.getFileName() + ":"
						+ s.getLineNumber() + "\n");
			}

		}
	}

}
