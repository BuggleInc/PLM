package plm.universe.bugglequest.operations;

import plm.universe.bugglequest.AbstractBuggle;

public class NoBaggleUnderBuggle extends BuggleOperation {
	
	public NoBaggleUnderBuggle(AbstractBuggle buggle) {
		super("noBaggleUnderBuggle", buggle.getName());
	}
}