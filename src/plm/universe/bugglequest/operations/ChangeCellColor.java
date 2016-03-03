package plm.universe.bugglequest.operations;

import java.awt.Color;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import plm.core.model.json.CustomColorSerializer;
import plm.universe.bugglequest.BuggleWorldCell;

public class ChangeCellColor extends BuggleWorldCellOperation{

	@JsonSerialize(using = CustomColorSerializer.class)
	private Color oldColor;
	@JsonSerialize(using = CustomColorSerializer.class)
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
}
