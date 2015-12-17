package plm.universe.bugglequest.operations;

import plm.universe.Operation;
import plm.universe.bugglequest.AbstractBuggle;

public abstract class BuggleOperation extends Operation {
	
	private AbstractBuggle buggle;
	
	public BuggleOperation(String name, AbstractBuggle buggle) {
		super(name);
		this.buggle = buggle;
	}
	
	public AbstractBuggle getBuggle() {
		return buggle;
	}
}
