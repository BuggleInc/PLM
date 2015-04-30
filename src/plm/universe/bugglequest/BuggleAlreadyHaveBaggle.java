package plm.universe.bugglequest;

import org.xnap.commons.i18n.I18n;

public class BuggleAlreadyHaveBaggle extends BuggleOperation {
	
	public BuggleAlreadyHaveBaggle(AbstractBuggle buggle, I18n i18n) {
		super("buggleAlreadyHaveBaggle", buggle);
		setMsg(i18n.tr("Buggle {0} tried to pick up a baggle while he had already one...", buggle.getName()));
	}
}