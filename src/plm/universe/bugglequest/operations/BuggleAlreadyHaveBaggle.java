package plm.universe.bugglequest.operations;

import plm.universe.bugglequest.AbstractBuggle;

public class BuggleAlreadyHaveBaggle extends BuggleOperation {
	
	public BuggleAlreadyHaveBaggle(AbstractBuggle buggle) {
		super("buggleAlreadyHaveBaggle", buggle.getName());
	}
}