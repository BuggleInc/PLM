package plm.core;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * An exception thrown when trying to compile Java programs from strings
 * containing source.
 * 
 * @author <a href="mailto:David.Biesack@sas.com">David J. Biesack</a>
 */
public class PLMCompilerException extends Exception {
	   private static final long serialVersionUID = 1L;
	   /**
	    * The fully qualified name of the class that was being compiled.
	    */
	   private Set<String> classNames;
	   // Unfortunately, Diagnostic and Collector are not Serializable, so we can't
	   // serialize the collector.
	   transient private DiagnosticCollector<JavaFileObject> diagnostics;

	   public PLMCompilerException(String message,
	         Set<String> qualifiedClassNames, Throwable cause,
	         DiagnosticCollector<JavaFileObject> diagnostics) {
	      super(message, cause);
	      setClassNames(qualifiedClassNames);
	      setDiagnostics(diagnostics);
	   }

	   public PLMCompilerException(String message,
	         Set<String> qualifiedClassNames,
	         DiagnosticCollector<JavaFileObject> diagnostics) {
	      super(message);
	      setClassNames(qualifiedClassNames);
	      setDiagnostics(diagnostics);
	   }

	   public PLMCompilerException(Set<String> qualifiedClassNames,
	         Throwable cause, DiagnosticCollector<JavaFileObject> diagnostics) {
	      super(cause);
	      setClassNames(qualifiedClassNames);
	      setDiagnostics(diagnostics);
	   }

	   public PLMCompilerException(String message) {
		   super(message);
	   }

	private void setClassNames(Set<String> qualifiedClassNames) {
	      // Creates a new HashSet because the set passed in may not be Serializable.
	      // For example, Map.keySet() returns a non-Serializable set.
		   if (qualifiedClassNames != null)
			   classNames = new HashSet<String>(qualifiedClassNames);
	   }

	   private void setDiagnostics(DiagnosticCollector<JavaFileObject> diagnostics) {
	      this.diagnostics = diagnostics;
	   }

	   /**
	    * Gets the diagnostics collected by this exception.
	    * 
	    * @return this exception's diagnostics
	    */
	   public DiagnosticCollector<JavaFileObject> getDiagnostics() {
	      return diagnostics;
	   }
	   
	   /**
	    * @return The name of the classes whose compilation caused the compile
	    *         exception
	    */
	   public Collection<String> getClassNames() {
	      return Collections.unmodifiableSet(classNames);
	   }
}
