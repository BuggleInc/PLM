package universe.bugglequest.exception;

import jlm.exception.JLMException;


public class AlreadyHaveBaggleException extends JLMException {

	private static final long serialVersionUID = -4857249506281940595L;

	public AlreadyHaveBaggleException(String msg) {
		super(msg);
	}
}
