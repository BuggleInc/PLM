package plm.universe.bugglequest;

public class BuggleAlreadyHaveBaggle extends BuggleOperation {
	
	public BuggleAlreadyHaveBaggle(AbstractBuggle buggle) {
		super("buggleAlreadyHaveBaggle", buggle);
		setMsg(getI18n().tr("Buggle {0} tried to pick up a baggle while he had already one...", buggle.getName()));
	}
}