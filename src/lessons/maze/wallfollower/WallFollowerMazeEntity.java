package lessons.maze.wallfollower;

import jlm.universe.Direction;

@SuppressWarnings("unused")
public class WallFollowerMazeEntity extends jlm.universe.bugglequest.SimpleBuggle {
	private Direction uselessVariableExistingJustToMakeSureThatEclipseWontRemoveTheImport; /* If removed, user code can't use directions easily */

	@Override
	public void setX(int i)  {
		if (isInited()) 
			throw new RuntimeException("setX(int) forbidden in this exercise");
		super.setX(i);
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException("setY(int) forbidden in this exercise");
		super.setY(i);
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException("setPos(int,int) forbidden in this exercise");
		super.setPos(i, j);
	}
	/*	// Stop moving if stepping over a baggle; commented because overriding forward is beyond the object know how of the students at this point 
	public void forward() {
	    if (!isOverBaggle()) 
	      super.forward();
	}
	 */
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

