package plm.core;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
  private Set<String> classNames = Set.of();
  /** The number of lines of the generated source before the code of the editor (see ProgrammingLanguage.Compiled) */
  private int lineShift;

  public PLMCompilerException(String message, Set<String> qualifiedClassNames, Throwable cause)
  {
    super(message, cause);
    setClassNames(qualifiedClassNames);
  }

  public PLMCompilerException(String message, Set<String> qualifiedClassNames)
  {
    super(message);
    setClassNames(qualifiedClassNames);
  }

  public PLMCompilerException(Set<String> qualifiedClassNames, Throwable cause)
  {
    super(cause);
    setClassNames(qualifiedClassNames);
  }

  public PLMCompilerException(String message) { super(message); }

  /** The messages of a compiler, whose locations in the code of the editor exceed its lines by lineShift */
  public PLMCompilerException(String message, Set<String> qualifiedClassNames, int lineShift)
  {
    this(message, qualifiedClassNames);
    this.lineShift = lineShift;
  }

  private void setClassNames(Set<String> qualifiedClassNames)
  {
    // Creates a new HashSet because the set passed in may not be Serializable.
    // For example, Map.keySet() returns a non-Serializable set.
    if (qualifiedClassNames != null)
      classNames = new HashSet<String>(qualifiedClassNames);
  }

  /**
   * @return The name of the classes whose compilation caused the compile
   *         exception
   */
  public Collection<String> getClassNames() { return Collections.unmodifiableSet(classNames); }

  public int getLineShift() { return lineShift; }
}
