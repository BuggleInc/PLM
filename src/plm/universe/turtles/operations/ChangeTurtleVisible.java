package plm.universe.turtles.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeTurtleVisible extends TurtleOperation {
	private boolean oldVisible, newVisible;
	
	@JsonCreator
	public ChangeTurtleVisible(@JsonProperty("turtleID")String turtleID,
			@JsonProperty("oldVisible")boolean oldVisible,
			@JsonProperty("newVisible")boolean newVisible) {
		super("changeTurtleVisible", turtleID);
		this.oldVisible = oldVisible;
		this.newVisible = newVisible;
	}

	public boolean getOldVisible() {
		return oldVisible;
	}

	public boolean getNewVisible() {
		return newVisible;
	}
}
