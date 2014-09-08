package plm.core.model.lesson;

import java.util.Date;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;

/** Class representing the result of pressing on the "run" button. Either a compilation error, or a percentage of passed/failed tests + a descriptive message */ 
public class ExecutionProgress {

	public static enum outcomeKind { COMPILE, FAIL, PASS };
	public outcomeKind outcome;
	
	public String compilationError;
	public String executionError = "";
	public int passedTests, totalTests=0;
	public Date date = new Date();
	public ProgrammingLanguage language = Game.getProgrammingLanguage();

	/* The feedback from the student in the ExecisePassedDialog */
	public String feedbackDifficulty;
	public String feedbackInterest;
	public String feedback; 

	public static ExecutionProgress newCompilationError(String message) {
		ExecutionProgress ep = new ExecutionProgress();
		
		ep.compilationError = message;
		ep.passedTests = -1;
		ep.totalTests = -1;
		if (ep.compilationError == null)
			ep.compilationError = "Unknown compilation error";
		ep.outcome = ExecutionProgress.outcomeKind.COMPILE;

		return ep;
	}
	public static ExecutionProgress newCompilationError(DiagnosticCollector<JavaFileObject> diagnostics) {
		StringBuffer sb = new StringBuffer();
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {			
			sb.append(diagnostic.getSource().getName()+":"+(diagnostic.getLineNumber()-1)+":"+ diagnostic.getMessage(null)); // -1 because the head is on the first line so the student code begins at line 2
			sb.append("\n");
		}
		return newCompilationError(sb.toString());
	}
	
	public void setCompilationError(String msg) {
		compilationError = msg;
		passedTests = -1;
	}


}
