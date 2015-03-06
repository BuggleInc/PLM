package plm.universe.bugglequest;

public class BuggleDontHaveBaggle extends BuggleOperation {
	
	public BuggleDontHaveBaggle(AbstractBuggle buggle) {
		super("buggleDontHaveBaggle", buggle);
		setMsg(getI18n().tr("Buggle {0} tried to drop a baggle but he doesn\'t have one...", buggle.getName()));
	}
}
