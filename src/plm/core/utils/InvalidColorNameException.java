package plm.core.utils;

public class InvalidColorNameException extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidColorNameException(String msg){
		super(msg);
	}
	public InvalidColorNameException(String msg, Exception e) {
		super(msg,e);
	}
}
