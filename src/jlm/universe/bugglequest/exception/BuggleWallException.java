package jlm.universe.bugglequest.exception;

import jlm.core.JLMException;


public class BuggleWallException extends JLMException {

	public BuggleWallException() {
		super("Buggles cannot traverse walls");
	}

	private static final long serialVersionUID = -7246709356730960089L;

}
