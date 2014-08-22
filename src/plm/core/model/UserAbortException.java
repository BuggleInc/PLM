package plm.core.model;


/**
 * Exception raised when the user cancels the "Quit" procedure.
 */
public class UserAbortException extends Exception {
	private static final long serialVersionUID = 1L;
	public UserAbortException(String msg, Exception ex) {
		super(msg,ex);
	}
}
