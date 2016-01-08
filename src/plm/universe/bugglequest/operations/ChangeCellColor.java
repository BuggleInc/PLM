package plm.universe.bugglequest.operations;

import java.awt.Color;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import plm.universe.bugglequest.BuggleWorldCell;

public class ChangeCellColor extends BuggleWorldCellOperation{

	private Color oldColor;
	private Color newColor;

	public ChangeCellColor(BuggleWorldCell cell, Color oldColor, Color newColor) {
		super("changeCellColor", cell);
		this.oldColor = oldColor;
		this.newColor = newColor;
	}

	public Color getOldColor() {
		return oldColor;
	}

	public Color getNewColor() {
		return newColor;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();

		JSONArray jsonOldColor = new JSONArray();
		jsonOldColor.add(oldColor.getRed());
		jsonOldColor.add(oldColor.getGreen());
		jsonOldColor.add(oldColor.getBlue());

		JSONArray jsonNewColor = new JSONArray();
		jsonNewColor.add(newColor.getRed());
		jsonNewColor.add(newColor.getGreen());
		jsonNewColor.add(newColor.getBlue());

		json.put("oldColor", jsonOldColor);
		json.put("newBodyColor", jsonNewColor);
		return json;
	}
}
