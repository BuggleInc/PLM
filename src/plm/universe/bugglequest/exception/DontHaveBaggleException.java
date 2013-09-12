package plm.universe.bugglequest.exception;

import plm.core.PLMException;
import plm.core.model.Game;

public class DontHaveBaggleException extends PLMException {
	private static final long serialVersionUID = 1L;

	public DontHaveBaggleException() {
		super(Game.i18n.tr("You have no baggle to drop"));
	
	}
}
