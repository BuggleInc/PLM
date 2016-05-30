package plm.universe.bugglequest;

public class BuggleInOuterSpace extends BuggleOperation {
		
	public BuggleInOuterSpace(AbstractBuggle buggle, String msg) {
		super("buggleInOuterSpace", buggle);
		setMsg(msg);
	}
}
