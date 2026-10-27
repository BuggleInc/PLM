package plm.universe;

/** Thrown by an entity that refuses a call on purpose (see the API gating): its message explains why to the student. */
public class PrimitiveRefusedException extends ExplainedException {
  public PrimitiveRefusedException(String message) { super(message); }
}
