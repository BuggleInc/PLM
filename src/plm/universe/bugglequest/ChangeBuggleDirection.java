package plm.universe.bugglequest;

import org.xnap.commons.i18n.I18n;

import plm.universe.Direction;

public class ChangeBuggleDirection extends BuggleOperation {
	private Direction oldDirection;
	private Direction newDirection;
	
	public ChangeBuggleDirection(AbstractBuggle buggle, Direction oldDirection, Direction newDirection, I18n i18n) {
		super("changeBuggleDirection", buggle);
		this.oldDirection = oldDirection;
		this.newDirection = newDirection;
		
		String direction = "";
		switch(this.newDirection.intValue()) {
			case Direction.NORTH_VALUE:
				direction = "north";
				break;
			case Direction.EAST_VALUE:
				direction = "east";
				break;
			case Direction.SOUTH_VALUE:
				direction = "south";
				break;
			case Direction.WEST_VALUE:
				direction = "west";
				break;
		}
		String msg = i18n.tr("Buggle {0} now faces the {1}.", buggle.getName(), direction);
		setMsg(msg);
	}

	public Direction getOldDirection() {
		return oldDirection;
	}

	public Direction getNewDirection() {
		return newDirection;
	}
}
