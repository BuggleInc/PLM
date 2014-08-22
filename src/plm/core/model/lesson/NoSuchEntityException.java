package plm.core.model.lesson;

import plm.core.PLMException;

public class NoSuchEntityException extends PLMException {
	private static final long serialVersionUID = 1L;
	
	public NoSuchEntityException(String msg) {
		super(msg);
	}
}
