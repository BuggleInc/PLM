package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveBuggleOperation extends BuggleOperation {

	private int oldX;
	private int oldY;
	private int newX;
	private int newY;

	@JsonCreator
	public MoveBuggleOperation(@JsonProperty("buggleID")String buggleID, @JsonProperty("oldX")int oldX,
			@JsonProperty("oldY")int oldY, @JsonProperty("newX")int newX, @JsonProperty("newY")int newY) {
		super("moveBuggleOperation", buggleID);
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
}
