package jlm.core.model;

/**
 * Exception raised when the user cancels the "Quit" procedure.
 */
public class UserAbortException extends Exception {
	private static final long serialVersionUID = 1L;
	public UserAbortException(String msg) {
		super(msg);
	}
}
