package lessons.backtracking;

/** This exception is raised when we do an invalid action (like reverting something we didn't do) */
public class InvalidBacktrackingActionException extends RuntimeException {
	public InvalidBacktrackingActionException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;

}
