package jlm.core.model.lesson;

import jlm.core.JLMException;

public class NoSuchEntityException extends JLMException {
	private static final long serialVersionUID = 1L;
	
	public NoSuchEntityException(String msg) {
		super(msg);
	}
}
