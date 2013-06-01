package jlm.core.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import jlm.core.model.Game;
import jlm.core.model.LogWriter;

import java.util.Locale;
import jlm.core.HumanLangChangesListener;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;


public class LoggerPanel extends JTextArea implements LogWriter, HumanLangChangesListener {

	private static final long serialVersionUID = 468774822833769775L;
	
	public I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);


	public LoggerPanel(Game game) {
		super();
		setEditable(false);
		setToolTipText(i18n.tr("Where error and other messages get written"));
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
		boolean warnedJava6= false;
		Pattern isJava6Pattern = Pattern.compile("major version 51 is newer than 50, the highest major version supported by this compiler");
		
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
			String source = diagnostic.getSource() == null ? "(null)" : diagnostic.getSource().getName();
			String msg = diagnostic.getMessage(getLocale());
			
			Matcher isJava6Matcher = isJava6Pattern.matcher(msg);
			if (isJava6Matcher.find()) {
				if (!warnedJava6 && Game.getInstance().isDebugEnabled())
					append("You are using a JLM jarfile that was compiled for Java 6, but you have a Java 7 runtime. This is *believed* to work.\n");
				warnedJava6 = true;
			} else {
				append(source+":"+diagnostic.getLineNumber()+":"+ msg+"\n");
			}
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

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		i18n.setLocale(newLang);
		
		setToolTipText(i18n.tr("Where error and other messages get written"));
		
	}

}
