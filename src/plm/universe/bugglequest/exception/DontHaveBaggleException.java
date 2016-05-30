package plm.universe.bugglequest.exception;

import org.xnap.commons.i18n.I18n;

import plm.core.PLMException;

public class DontHaveBaggleException extends PLMException {
	private static final long serialVersionUID = 1L;

	public DontHaveBaggleException(I18n i18n) {
		super(i18n.tr("You have no baggle to drop"));
	
	}
}
