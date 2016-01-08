package plm.universe.bugglequest.operations;

import org.json.simple.JSONObject;

import plm.universe.bugglequest.AbstractBuggle;

public class ChangeBuggleBrushDown extends BuggleOperation {
	private boolean oldBrushDown;
	private boolean newBrushDown;

	public ChangeBuggleBrushDown(AbstractBuggle buggle, boolean oldBrushDown, boolean newBrushDown) {
		super("changeBuggleBrushDown", buggle.getName());
		this.oldBrushDown = oldBrushDown;
		this.newBrushDown = newBrushDown;
	}

	public boolean getOldBrushDown() {
		return oldBrushDown;
	}

	public boolean getNewBrushDown() {
		return newBrushDown;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("oldBrushDown", oldBrushDown);
		json.put("newBrushDown", newBrushDown);
		return json;
	}
}