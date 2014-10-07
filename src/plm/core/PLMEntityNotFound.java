package plm.core;

public class PLMEntityNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PLMEntityNotFound(String string) {
		super(string);
	}
	
	public PLMEntityNotFound(String string, Throwable e) {
		super(string, e);
	}
}
