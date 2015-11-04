package plm.universe.bugglequest;

public class MoveBuggleOperation extends BuggleOperation {

	private int oldX;
	private int oldY;
	private int newX;
	private int newY;

	public MoveBuggleOperation(AbstractBuggle buggle, int oldX,
			int oldY, int newX, int newY) {
		super("moveBuggleOperation", buggle);
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
