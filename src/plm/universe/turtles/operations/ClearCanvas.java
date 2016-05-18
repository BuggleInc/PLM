package plm.universe.turtles.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClearCanvas extends TurtleOperation {

	@JsonCreator
	public ClearCanvas(@JsonProperty("turtleID")String turtleID) {
		super("clearCanvas", turtleID);
	}
}
