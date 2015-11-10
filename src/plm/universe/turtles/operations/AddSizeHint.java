package plm.universe.turtles.operations;

import plm.universe.turtles.Turtle;

public class AddSizeHint extends TurtleOperation {

	private int x1, y1, x2, y2;
	private String text;
	
	public AddSizeHint(Turtle turtle, int x1, int y1, int x2, int y2, String text) {
		super("addSizeHint", turtle);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.text = text;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public String getText() {
		return text;
	}

}
