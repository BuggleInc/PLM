package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import plm.core.model.json.CustomDirectionSerializer;
import plm.universe.Direction;
import plm.universe.bugglequest.AbstractBuggle;

public class ChangeBuggleDirection extends BuggleOperation {
	@JsonSerialize(using = CustomDirectionSerializer.class)
	private Direction oldDirection;
	@JsonSerialize(using = CustomDirectionSerializer.class)
	private Direction newDirection;

	public ChangeBuggleDirection(AbstractBuggle buggle, Direction oldDirection, Direction newDirection) {
		super("changeBuggleDirection", buggle.getName());
		this.oldDirection = oldDirection;
		this.newDirection = newDirection;
	}

	public Direction getOldDirection() {
		return oldDirection;
	}

	public Direction getNewDirection() {
		return newDirection;
	}
}
