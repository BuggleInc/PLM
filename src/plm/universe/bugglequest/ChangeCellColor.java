package plm.universe.bugglequest;

import java.awt.Color;

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
}
