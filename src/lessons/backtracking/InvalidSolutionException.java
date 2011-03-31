package lessons.backtracking;

/** This exception is raised when we are asked to evaluate a solution which reveals invalid */
public class InvalidSolutionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidSolutionException(String msg) {
		super(msg);
	}

}
