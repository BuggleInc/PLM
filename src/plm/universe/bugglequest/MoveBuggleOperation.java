package plm.universe.bugglequest;

import org.xnap.commons.i18n.I18n;

public class MoveBuggleOperation extends BuggleOperation {
	
	private int oldX;
	private int oldY;
	private int newX;
	private int newY;
	
	public MoveBuggleOperation(AbstractBuggle buggle, int oldX,
			int oldY, int newX, int newY, I18n i18n) {
		super("moveBuggleOperation", buggle);
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		
		Object args[] = { buggle.getName(), oldX, oldY, newX, newY };
		String msg = i18n.tr("Buggle {0} moved from ({1}, {2}) to ({3}, {4})", args);
		setMsg(msg);
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
