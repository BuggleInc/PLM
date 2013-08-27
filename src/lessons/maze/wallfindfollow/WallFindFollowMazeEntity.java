package lessons.maze.wallfindfollow;

import plm.core.model.Game;
import plm.universe.Direction;

@SuppressWarnings("unused")
public class WallFindFollowMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
	private Direction uselessVariableExistingJustToMakeSureThatEclipseWontRemoveTheImport; /* If removed, user code can't use directions easily */
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setX() in this exercise."));
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setY() in this exercise."));
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setPos() in this exercise."));
	}
	 
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	public void stepHandOnWall() {
		// PRE: we have a wall on the left
		// POST: we still have the same wall on the left, are one step ahead

		while (!isFacingWall()) {
			forward();
			left(); // change to right to get a right follower
		}
		right(); // change to left to get a right follower
	}

	public void run() {
		// Make sure we have a wall to the left
		left();
		while (!isFacingWall())
			forward();
		right();

		while (!isOverBaggle())
			stepHandOnWall();

		pickupBaggle();
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}

