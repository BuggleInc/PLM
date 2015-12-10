package lessons.maze.wallfollower;

import plm.core.model.Game;
import plm.universe.Direction;

@SuppressWarnings("unused")
public class WallFollowerMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
	private Direction uselessVariableExistingJustToMakeSureThatEclipseWontRemoveTheImport; /* If removed, user code can't use directions easily */
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead."));
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead."));
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead."));
	}
	 
	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		// Make sure we have a wall to the left
		left();
		while (!isFacingWall())
			forward();
		right();

		while (!isOverBaggle())
			stepHandOnWall();

		pickupBaggle();
	}
	
	public void stepHandOnWall() {
		// PRE: we have a wall on the left
		// POST: we still have the same wall on the left, are one step ahead

		while (!isFacingWall()) {
			forward();
			left(); // change to right to get a right follower
		}
		right(); // change to left to get a right follower
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}

