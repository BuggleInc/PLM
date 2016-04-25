package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import plm.core.model.json.CustomDirectionSerializer;
import plm.universe.Direction;

public class ChangeBuggleDirection extends BuggleOperation {
	@JsonSerialize(using = CustomDirectionSerializer.class)
	private Direction oldDirection;
	@JsonSerialize(using = CustomDirectionSerializer.class)
	private Direction newDirection;

	@JsonCreator
	public ChangeBuggleDirection(@JsonProperty("buggleID")String buggleID, @JsonProperty("oldDirection")Direction oldDirection, @JsonProperty("newDirection")Direction newDirection) {
		super("changeBuggleDirection", buggleID);
		this.oldDirection = oldDirection;
		this.newDirection = newDirection;
	}

	public Direction getOldDirection() {
		return oldDirection;
	}

	public Direction getNewDirection() {
		return newDirection;
	}

	public void setOldDirection(Direction oldDirection) {
		this.oldDirection = oldDirection;
	}

	public void setNewDirection(Direction newDirection) {
		this.newDirection = newDirection;
	}
}
