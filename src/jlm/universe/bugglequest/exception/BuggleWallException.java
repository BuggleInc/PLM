package jlm.universe.bugglequest.exception;

import jlm.core.JLMException;
import jlm.core.model.Game;


public class BuggleWallException extends JLMException {

	public BuggleWallException() {
		super(Game.i18n.tr("Buggles cannot traverse walls"));
	}

	private static final long serialVersionUID = -7246709356730960089L;

}
