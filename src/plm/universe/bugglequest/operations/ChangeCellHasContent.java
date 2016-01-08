package plm.universe.bugglequest.operations;

import org.json.simple.JSONObject;

import plm.universe.bugglequest.BuggleWorldCell;

public class ChangeCellHasContent extends BuggleWorldCellOperation{

	private boolean oldHasContent;
	private boolean newHasContent;

	public ChangeCellHasContent(BuggleWorldCell cell, boolean oldHasContent, boolean newHasContent) {
		super("changeCellHasContent", cell);
		this.oldHasContent = oldHasContent;
		this.newHasContent = newHasContent;
	}

	public boolean getOldHasContent() {
		return oldHasContent;
	}

	public boolean getNewHasContent() {
		return newHasContent;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();

		json.put("oldHasContent", oldHasContent);
		json.put("newHasContent", newHasContent);
		return json;
	}
}