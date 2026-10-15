package plm.universe;

/** Thrown by an entity that refuses a call on purpose: its message explains why to the student and is shown as is. */
public class PrimitiveRefusedException extends UnsupportedOperationException {
  public PrimitiveRefusedException(String message) { super(message); }
}
