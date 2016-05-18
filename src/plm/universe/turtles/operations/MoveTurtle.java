package plm.universe.turtles.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveTurtle extends TurtleOperation {

	private double oldX, oldY, newX, newY;
	
	@JsonCreator
	public MoveTurtle(@JsonProperty("turtleID")String turtleID,
			@JsonProperty("oldX")double oldX, @JsonProperty("oldY")double oldY,
			@JsonProperty("newX")double newX, @JsonProperty("newY")double newY) {
		super("moveTurtle", turtleID);
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
