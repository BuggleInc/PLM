package plm.universe.bugglequest.exception;

import plm.core.PLMException;

public class BuggleWallException extends PLMException {

	// FIXME: Re-enable translation
	public BuggleWallException(/* I18n i18n */) {
		super(/*i18n.tr(*/"Buggles cannot traverse walls"/*)*/);
	}

	private static final long serialVersionUID = -7246709356730960089L;

}
