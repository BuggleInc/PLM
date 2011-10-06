package jlm.core.model.lesson;

import java.util.Date;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;

/** Class representing the result of pressing on the "run" button. Either a compilation error, or a percentage of passed/failed tests + a descriptive message */ 
public class ExecutionProgress {
	public String compilationError;
	public int passedTests, totalTests;
	public String details = "";
	public Date date = new Date();
	public ProgrammingLanguage language = Game.getProgrammingLanguage();

	public static ExecutionProgress newCompilationError(
			DiagnosticCollector<JavaFileObject> diagnostics) {
		ExecutionProgress ep = new ExecutionProgress();
		StringBuffer sb = new StringBuffer();
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {			
			sb.append(diagnostic.getSource().getName()+":"+diagnostic.getLineNumber()+":"+ diagnostic.getMessage(null));
			sb.append("\n");
		}

		ep.compilationError = sb.toString();
		ep.passedTests = -1;
		ep.totalTests = 0;
		if (ep.compilationError == null)
			ep.compilationError = "Unknown compilation error";
		return ep;
	}

}
