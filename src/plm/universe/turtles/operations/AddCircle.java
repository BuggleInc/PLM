package plm.universe.turtles.operations;

import java.awt.Color;

import plm.universe.turtles.Turtle;

public class AddCircle extends TurtleOperation {

	private double x, y, radius;
	private Color color;
	
	public AddCircle(Turtle turtle, double x, double y, double radius, Color color) {
		super("addCircle", turtle);
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getRadius() {
		return radius;
	}

	public Color getColor() {
		return color;
	}
	
}
