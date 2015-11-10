package plm.universe.turtles.operations;

import plm.universe.turtles.Turtle;

public class RotateTurtle extends TurtleOperation {

	private double oldHeading, newHeading;
	
	public RotateTurtle(Turtle turtle, double oldHeading, double newHeading) {
		super("rotateTurtle", turtle);
		this.oldHeading = oldHeading;
		this.newHeading = newHeading;
	}

	public double getOldHeading() {
		return oldHeading;
	}

	public double getNewHeading() {
		return newHeading;
	}
}
