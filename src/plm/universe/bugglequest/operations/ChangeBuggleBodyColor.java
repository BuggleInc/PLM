package plm.universe.bugglequest.operations;

import java.awt.Color;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import plm.core.model.json.CustomColorSerializer;
import plm.universe.bugglequest.AbstractBuggle;

public class ChangeBuggleBodyColor extends BuggleOperation {
	
	@JsonSerialize(using = CustomColorSerializer.class)
	private Color oldBodyColor;
	@JsonSerialize(using = CustomColorSerializer.class)
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
}