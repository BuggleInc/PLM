package plm.universe.bugglequest.operations;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import plm.core.model.json.CustomColorSerializer;

public class ChangeBuggleBodyColor extends BuggleOperation {
	
	@JsonSerialize(using = CustomColorSerializer.class)
	private Color oldBodyColor;
	@JsonSerialize(using = CustomColorSerializer.class)
	private Color newBodyColor;
	
	@JsonCreator
	public ChangeBuggleBodyColor(@JsonProperty("buggleID")String buggleID, @JsonProperty("oldBodyColor")Color oldBodyColor, @JsonProperty("newBodyColor")Color newBodyColor) {
		super("changeBuggleBodyColor", buggleID);
		this.oldBodyColor = oldBodyColor;
		this.newBodyColor = newBodyColor;
	}

	public Color getOldBodyColor() {
		return oldBodyColor;
	}

	public Color getNewBodyColor() {
		return newBodyColor;
	}

	public void setOldBodyColor(Color oldBodyColor) {
		this.oldBodyColor = oldBodyColor;
	}

	public void setNewBodyColor(Color newBodyColor) {
		this.newBodyColor = newBodyColor;
	}
}