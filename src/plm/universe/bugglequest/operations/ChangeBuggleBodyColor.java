package plm.universe.bugglequest.operations;

import java.awt.Color;

import plm.universe.bugglequest.AbstractBuggle;

public class ChangeBuggleBodyColor extends BuggleOperation {
	private Color oldBodyColor;
	private Color newBodyColor;
	
	public ChangeBuggleBodyColor(AbstractBuggle buggle, Color oldBodyColor, Color newBodyColor) {
		super("changeBuggleBodyColor", buggle);
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