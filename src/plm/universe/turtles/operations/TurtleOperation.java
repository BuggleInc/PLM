package plm.universe.turtles.operations;

import plm.universe.Operation;

public abstract class TurtleOperation extends Operation
{
	
	private String turtleID;

	public TurtleOperation(String name, String turtleID) {
		super(name);
		this.turtleID = turtleID;
	}
	
	public String getTurtleID() {
		return turtleID;
	}
	
	public void setTurtleID(String turtleID) {
		this.turtleID = turtleID;
	}
}
