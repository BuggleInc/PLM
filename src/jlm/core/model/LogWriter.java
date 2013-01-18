package jlm.core.model;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * Interface that all output consoles that we may use must implement.   
 */
public interface LogWriter {

	public void log(String msg) ;

	public void log(DiagnosticCollector<JavaFileObject> diagnostics) ;
	
	public void log(Exception e) ;
}
