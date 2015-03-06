package plm.universe.bugglequest;

public class NoBaggleUnderBuggle extends BuggleOperation {
	
	public NoBaggleUnderBuggle(AbstractBuggle buggle) {
		super("noBaggleUnderBuggle", buggle);
		String msg = getI18n().tr("Buggle {0} tried to pick up a baggle while there were none...", buggle.getName());
		setMsg(msg);
	}
}