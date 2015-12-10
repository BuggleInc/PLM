package plm.universe.bugglequest;

import org.xnap.commons.i18n.I18n;

public class NoBaggleUnderBuggle extends BuggleOperation {
	
	public NoBaggleUnderBuggle(AbstractBuggle buggle, I18n i18n) {
		super("noBaggleUnderBuggle", buggle);
		String msg = i18n.tr("Buggle {0} tried to pick up a baggle while there were none...", buggle.getName());
		setMsg(msg);
	}
}