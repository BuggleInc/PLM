package plm.universe.bugglequest.operations;

import org.json.simple.JSONObject;

import plm.universe.bugglequest.BuggleWorldCell;

public class ChangeCellContent extends BuggleWorldCellOperation{

	private String oldContent;
	private String newContent;

	public ChangeCellContent(BuggleWorldCell cell, String oldContent, String newContent) {
		super("changeCellContent", cell);
		this.oldContent = oldContent;
		this.newContent = newContent;
	}

	public String getOldContent() {
		return oldContent;
	}

	public String getNewContent() {
		return newContent;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
				
		json.put("oldContent", oldContent);
		json.put("newContent", newContent);
		return json;
	}
}