package plm.universe.bugglequest.operations;

import java.awt.Color;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import plm.core.utils.ColorMapper;
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
		
		JSONArray jsonOldBodyColor = ColorMapper.color2json(oldBodyColor);
		JSONArray jsonNewBodyColor = ColorMapper.color2json(newBodyColor);
		
		json.put("oldBodyColor", jsonOldBodyColor);
		json.put("newBodyColor", jsonNewBodyColor);
		return json;
	}
}