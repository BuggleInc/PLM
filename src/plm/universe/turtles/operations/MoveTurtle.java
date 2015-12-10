package plm.universe.turtles.operations;

import plm.universe.turtles.Turtle;

public class MoveTurtle extends TurtleOperation {

	private double oldX, oldY, newX, newY;
	
	public MoveTurtle(Turtle turtle, double oldX, double oldY, double newX, double newY) {
		super("moveTurtle", turtle);
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
	}

	public double getOldX() {
		return oldX;
	}

	public double getOldY() {
		return oldY;
	}

	public double getNewX() {
		return newX;
	}

	public double getNewY() {
		return newY;
	}
}
