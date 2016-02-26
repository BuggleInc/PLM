package plm.universe.bugglequest.operations;

import plm.universe.Operation;
import plm.universe.bugglequest.AbstractBuggle;

public abstract class BuggleOperation extends Operation {

	private String buggleID;

	public BuggleOperation(String name, String buggleID) {
		super(name);
		this.buggleID = buggleID;
	}

	public AbstractBuggle getBuggle() {
		return null;
	}
}
