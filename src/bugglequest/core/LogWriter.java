package bugglequest.core;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public interface LogWriter {

	public void log(String msg) ;

	public void log(DiagnosticCollector<JavaFileObject> diagnostics) ;
	
	public void log(Exception e) ;
}
