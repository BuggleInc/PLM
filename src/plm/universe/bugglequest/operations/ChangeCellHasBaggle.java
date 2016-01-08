package plm.universe.bugglequest.operations;

import org.json.simple.JSONObject;

import plm.universe.bugglequest.BuggleWorldCell;

public class ChangeCellHasBaggle extends BuggleWorldCellOperation{

	private boolean oldHasBaggle;
	private boolean newHasBaggle;

	public ChangeCellHasBaggle(BuggleWorldCell cell, boolean oldHasBaggle, boolean newHasBaggle) {
		super("changeCellHasBaggle", cell);
		this.oldHasBaggle = oldHasBaggle;
		this.newHasBaggle = newHasBaggle;
	}

	public boolean getOldHasBaggle() {
		return oldHasBaggle;
	}

	public boolean getNewHasBaggle() {
		return newHasBaggle;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();

		json.put("oldHasBaggle", oldHasBaggle);
		json.put("newHasBaggle", newHasBaggle);
		return json;
	}
}
