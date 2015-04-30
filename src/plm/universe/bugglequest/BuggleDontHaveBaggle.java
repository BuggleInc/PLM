package plm.universe.bugglequest;

import org.xnap.commons.i18n.I18n;

public class BuggleDontHaveBaggle extends BuggleOperation {
	
	public BuggleDontHaveBaggle(AbstractBuggle buggle, I18n i18n) {
		super("buggleDontHaveBaggle", buggle);
		setMsg(i18n.tr("Buggle {0} tried to drop a baggle but he doesn\'t have one...", buggle.getName()));
	}
}
