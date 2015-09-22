package plm.universe.turtles.operations;

import java.awt.Color;

import plm.universe.turtles.Turtle;

public class AddLine extends TurtleOperation {

	private double x1, y1, x2, y2;
	private Color color;
	
	public AddLine(Turtle turtle, double x1, double y1, double x2, double y2, Color color) {
		super("addLine", turtle);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
	}

	public double getX1() {
		return x1;
	}

	public double getY1() {
		return y1;
	}

	public double getX2() {
		return x2;
	}

	public double getY2() {
		return y2;
	}

	public Color getColor() {
		return color;
	}
}
