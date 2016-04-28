package plm.universe.bugglequest.operations;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import plm.core.model.json.CustomColorSerializer;

public class ChangeCellColor extends BuggleWorldCellOperation{

	@JsonSerialize(using = CustomColorSerializer.class)
	private Color oldColor;
	@JsonSerialize(using = CustomColorSerializer.class)
	private Color newColor;

	@JsonCreator
	public ChangeCellColor(@JsonProperty("x")int x, @JsonProperty("y")int y, @JsonProperty("oldColor")Color oldColor, @JsonProperty("color")Color newColor) {
		super("changeCellColor", x, y);
		this.oldColor = oldColor;
		this.newColor = newColor;
	}

	public Color getOldColor() {
		return oldColor;
	}

	public Color getNewColor() {
		return newColor;
	}
}
