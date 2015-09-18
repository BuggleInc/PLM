package plm.universe.turtles.operations;

import plm.universe.turtles.Turtle;

public class ChangeTurtleVisible extends TurtleOperation {
	private boolean oldVisible, newVisible;
	
	public ChangeTurtleVisible(Turtle turtle, boolean oldVisible, boolean newVisible) {
		super("changeTurtleVisible", turtle);
		this.oldVisible = oldVisible;
		this.newVisible = newVisible;
	}

	public boolean getOldVisible() {
		return oldVisible;
	}

	public boolean getNewVisible() {
		return newVisible;
	}
}
