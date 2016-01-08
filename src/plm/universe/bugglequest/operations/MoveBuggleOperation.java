package plm.universe.bugglequest.operations;

import org.json.simple.JSONObject;

import plm.universe.bugglequest.AbstractBuggle;

public class MoveBuggleOperation extends BuggleOperation {

	private int oldX;
	private int oldY;
	private int newX;
	private int newY;

	public MoveBuggleOperation(AbstractBuggle buggle, int oldX,
			int oldY, int newX, int newY) {
		super("moveBuggleOperation", buggle.getName());
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
	}

	public int getOldX() {
		return oldX;
	}

	public int getOldY() {
		return oldY;
	}

	public int getNewX() {
		return newX;
	}

	public int getNewY() {
		return newY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("oldX", oldX);
		json.put("oldY", oldY);
		json.put("newX", newX);
		json.put("newY", newY);
		return json;
	}
}
