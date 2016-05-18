package plm.universe.turtles.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RotateTurtle extends TurtleOperation {

	private double oldHeading, newHeading;
	
	@JsonCreator
	public RotateTurtle(@JsonProperty("turtleID")String turtleID,
			@JsonProperty("oldHeading")double oldHeading,
			@JsonProperty("newHeading")double newHeading) {
		super("rotateTurtle", turtleID);
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
