package plm.universe.bugglequest;

public class ChangeBuggleCarryBaggle extends BuggleOperation {
	private boolean oldCarryBaggle;
	private boolean newCarryBaggle;
	
	public ChangeBuggleCarryBaggle(AbstractBuggle buggle, boolean oldCarryBaggle, boolean newCarryBaggle) {
		super("changeBuggleCarryBaggle", buggle);
		this.oldCarryBaggle = oldCarryBaggle;
		this.newCarryBaggle = newCarryBaggle;
		
		String msg = "";
		if(this.newCarryBaggle) {
			msg= getI18n().tr("Buggle {0} is now carrying a baggle!", buggle.getName());	
		}
		else {
			msg = getI18n().tr("Buggle {0} drops the baggle.", buggle.getName());
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