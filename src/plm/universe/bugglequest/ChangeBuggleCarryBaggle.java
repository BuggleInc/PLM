package plm.universe.bugglequest;

public class ChangeBuggleCarryBaggle extends BuggleOperation {
	private boolean oldCarryBaggle;
	private boolean newCarryBaggle;
	
	public ChangeBuggleCarryBaggle(AbstractBuggle buggle, boolean oldCarryBaggle, boolean newCarryBaggle) {
		super("changeBuggleCarryBaggle", buggle);
		this.oldCarryBaggle = oldCarryBaggle;
		this.newCarryBaggle = newCarryBaggle;
	}

	public boolean getOldCarryBaggle() {
		return oldCarryBaggle;
	}

	public boolean getNewCarryBaggle() {
		return newCarryBaggle;
	}
}