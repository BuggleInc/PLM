package plm.universe.bugglequest.operations;

import org.json.simple.JSONObject;

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

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("buggleID", buggleID);
		return json;
	}
}
