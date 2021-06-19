package plm.universe.bugglequest.exception;

import plm.core.PLMException;
import plm.core.model.Game;


public class BuggleWallException extends PLMException {

	public BuggleWallException() {
		super(Game.i18n.tr("Buggles cannot traverse walls"));
		System.err.println("Buggles cannot traverse walls");
	}

	private static final long serialVersionUID = -7246709356730960089L;

}
