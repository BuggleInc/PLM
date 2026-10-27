package plm.universe;

/**
 * An error of the student's program detected by the world or the entity, such as a deadlock or a refused primitive: its message
 * explains it to the student, and is shown as is instead of the call that raised it.
 */
public class ExplainedException extends RuntimeException {
  public ExplainedException(String message) { super(message); }
}
