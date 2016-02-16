package plm.core.model.lesson;

import java.util.Date;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.json.simple.JSONObject;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.I18nManager;
import plm.core.model.ToJSON;

/** Class representing the result of pressing on the "run" button. Either a compilation error, or a percentage of passed/failed tests + a descriptive message */ 
public class ExecutionProgress implements ToJSON {

	public static enum outcomeKind { COMPILE, FAIL, PASS, TIMEOUT };
	public outcomeKind outcome = outcomeKind.PASS;

	public String compilationError = "";
	public String executionError = "";
	public String commonErrorText = "";
	public int passedTests=0, totalTests=0, commonErrorID=-1;
	public Date date = new Date();
	public ProgrammingLanguage language;

	/* The feedback from the student in the ExecisePassedDialog */
	public String feedbackDifficulty;
	public String feedbackInterest;
	public String feedback; 

	private Locale locale;

	public ExecutionProgress(ProgrammingLanguage language) {
		this.language = language;
	}

	public ExecutionProgress(ProgrammingLanguage language, Locale locale) {
		this(language);
		this.locale = locale;
	}

	public ExecutionProgress(JSONObject json) {
		outcome = outcomeKind.valueOf((String) json.get("outcome"));
		compilationError = (String) json.get("compilationError");
		executionError = (String) json.get("executionError");
		passedTests = ((Long) json.get("passedTests")).intValue();
		totalTests = ((Long) json.get("totalTests")).intValue();
		date = new Date((Long) json.get("date"));
		language = ProgrammingLanguage.getProgrammingLanguage((String) json.get("language"));
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		
		json.put("outcome", outcome.toString());
		json.put("compilationError", compilationError);
		json.put("executionError", executionError);
		json.put("passedTests", passedTests);
		json.put("totalTests", totalTests);
		json.put("date", date.getTime());
		json.put("language", language.getLang());
		
		return json;
	}
	
	public static ExecutionProgress newCompilationError(String message, ProgrammingLanguage language) {
		ExecutionProgress ep = new ExecutionProgress(language);
		ep.compilationError = message;
		ep.passedTests = -1;
		ep.totalTests = -1;
		if (ep.compilationError == null)
			ep.compilationError = "Unknown compilation error";
		ep.outcome = ExecutionProgress.outcomeKind.COMPILE;

		return ep;
	}
	public static ExecutionProgress newCompilationError(DiagnosticCollector<JavaFileObject> diagnostics, ProgrammingLanguage language) {
		StringBuffer sb = new StringBuffer();
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {	
			if (diagnostic.getSource() == null) 
				sb.append("unknown source:"+ diagnostic.getMessage(null)); // -1 because the head is on the first line so the student code begins at line 2
			else
				sb.append(diagnostic.getSource().getName()+":"+(diagnostic.getLineNumber()-1)+":"+ diagnostic.getMessage(null)); // -1 because the head is on the first line so the student code begins at line 2
			sb.append("\n");
		}
		return newCompilationError(sb.toString(), language);
	}

	public void setCompilationError(String msg) {
		outcome = ExecutionProgress.outcomeKind.COMPILE;
		compilationError = I18nManager.getI18n(locale).tr("Compilation error:") + "\n" + msg;
		passedTests = -1;
	}

	public void setCompilationError(DiagnosticCollector<JavaFileObject> diagnostics) {
		StringBuffer sb = new StringBuffer();
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {	
			if (diagnostic.getSource() == null) 
				sb.append(I18nManager.getI18n(locale).tr("unknown source:")+ diagnostic.getMessage(null)); // -1 because the head is on the first line so the student code begins at line 2
			else
				sb.append(diagnostic.getSource().getName()+":"+(diagnostic.getLineNumber()-1)+":"+ diagnostic.getMessage(null)); // -1 because the head is on the first line so the student code begins at line 2
			sb.append("\n");
		}
		setCompilationError(sb.toString());
	}

	public void setExecutionError(String msg) {
		outcome = ExecutionProgress.outcomeKind.FAIL;
		executionError = msg;
	}

	public void setTimeoutError() {
		outcome = ExecutionProgress.outcomeKind.TIMEOUT;
		executionError = I18nManager.getI18n(locale).tr("The assessment of your code \"time out\". It may due to an infinite loop inside your program!\n") +
				I18nManager.getI18n(locale).tr("However, if your program doesn't seems to have started, it may indicates that our servers are overloaded. Sorry for the inconvenience and please wait a few moments before submitting your program again.");
	}

	public void setStopError() {
		outcome = ExecutionProgress.outcomeKind.FAIL;
		executionError = I18nManager.getI18n(locale).tr("You interrupted the execution, did you fall into an infinite loop ?\n") +
				I18nManager.getI18n(locale).tr("Your program must stop by itself to successfully pass the exercise.\n");
	}

	public String getMsg(Locale locale) {
		String res = "";
		if(outcome == ExecutionProgress.outcomeKind.PASS) {
			res = I18nManager.getI18n(locale).tr("Congratulations, you passed this exercise.({0} tests passed)", passedTests);
		}
		else if(outcome == ExecutionProgress.outcomeKind.COMPILE) {
			res = compilationError;
		}
		else {
			res = executionError;
		}
		return res;
	}
}
