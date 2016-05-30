package plm.universe.bugglequest;

import org.xnap.commons.i18n.I18n;

public class ChangeBuggleCarryBaggle extends BuggleOperation {
	private boolean oldCarryBaggle;
	private boolean newCarryBaggle;
	
	public ChangeBuggleCarryBaggle(AbstractBuggle buggle, boolean oldCarryBaggle, boolean newCarryBaggle, I18n i18n) {
		super("changeBuggleCarryBaggle", buggle);
		this.oldCarryBaggle = oldCarryBaggle;
		this.newCarryBaggle = newCarryBaggle;
		
		String msg = "";
		if(this.newCarryBaggle) {
			msg= i18n.tr("Buggle {0} is now carrying a baggle!", buggle.getName());	
		}
		else {
			msg = i18n.tr("Buggle {0} drops the baggle.", buggle.getName());
		}
		setMsg(msg);
	}

	public boolean getOldCarryBaggle() {
		return oldCarryBaggle;
	}

	public boolean getNewCarryBaggle() {
		return newCarryBaggle;
	}
}