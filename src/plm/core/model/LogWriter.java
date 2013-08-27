package plm.core.model;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * Interface that all output consoles that we may use must implement. 
 * 
 * There is only one of them, but I was told that interfaces are the right way for a model to speak of its view elements.
 */
public interface LogWriter {

	public void log(String msg) ;

	public void log(DiagnosticCollector<JavaFileObject> diagnostics) ;
	
	public void log(Exception e) ;
}
