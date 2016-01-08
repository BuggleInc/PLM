package plm.universe.bugglequest.operations;

import java.awt.Color;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import plm.universe.bugglequest.AbstractBuggle;

public class ChangeBuggleBodyColor extends BuggleOperation {
	private Color oldBodyColor;
	private Color newBodyColor;
	
	public ChangeBuggleBodyColor(AbstractBuggle buggle, Color oldBodyColor, Color newBodyColor) {
		super("changeBuggleBodyColor", buggle.getName());
		this.oldBodyColor = oldBodyColor;
		this.newBodyColor = newBodyColor;
	}

	public Color getOldBodyColor() {
		return oldBodyColor;
	}

	public Color getNewBodyColor() {
		return newBodyColor;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		
		JSONArray jsonOldBodyColor = new JSONArray();
		jsonOldBodyColor.add(oldBodyColor.getRed());
		jsonOldBodyColor.add(oldBodyColor.getGreen());
		jsonOldBodyColor.add(oldBodyColor.getBlue());
		
		JSONArray jsonNewBodyColor = new JSONArray();
		jsonNewBodyColor.add(newBodyColor.getRed());
		jsonNewBodyColor.add(newBodyColor.getGreen());
		jsonNewBodyColor.add(newBodyColor.getBlue());
		
		json.put("oldBodyColor", jsonOldBodyColor);
		json.put("newBodyColor", jsonNewBodyColor);
		return json;
	}
}