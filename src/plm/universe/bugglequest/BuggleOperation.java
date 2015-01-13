package plm.universe.bugglequest;

import plm.universe.Operation;

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
