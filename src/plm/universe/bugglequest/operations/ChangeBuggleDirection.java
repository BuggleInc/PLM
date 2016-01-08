package plm.universe.bugglequest.operations;

import org.json.simple.JSONObject;

import plm.universe.Direction;
import plm.universe.bugglequest.AbstractBuggle;

public class ChangeBuggleDirection extends BuggleOperation {
	private Direction oldDirection;
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

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();

		json.put("oldDirection", oldDirection.intValue());
		json.put("newDirection", newDirection.intValue());
		return json;
	}
}
