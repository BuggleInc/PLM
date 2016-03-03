package plm.universe.bugglequest.operations;

import plm.universe.Operation;

public abstract class BuggleOperation extends Operation {

	private String buggleID;

	public BuggleOperation(String name, String buggleID) {
		super(name);
		this.setBuggleID(buggleID);
	}

	public String getBuggleID() {
		return buggleID;
	}

	public void setBuggleID(String buggleID) {
		this.buggleID = buggleID;
	}
}
