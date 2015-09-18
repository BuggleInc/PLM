package plm.universe.turtles.operations;

import plm.universe.Operation;
import plm.universe.turtles.Turtle;

public abstract class TurtleOperation extends Operation
{
	
	private Turtle turtle;

	public TurtleOperation(String name, Turtle turtle) {
		super(name);
		this.turtle = turtle;
	}
	
	public Turtle getTurtle() {
		return turtle;
	}
	
}
