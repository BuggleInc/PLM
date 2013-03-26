package lessons.maze;

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
	/* BEGIN SOLUTION */
	public void stepHandOnWall() {
	    // PRE: we have a wall on the left
	    // POST: we still have the same wall on the left, are one step ahead

	   while (!isFacingWall()) {
	       forward();
	       turnLeft(); // change to turnRight to get a right follower
	   }
	   turnRight(); // change to turnLeft to get a right follower
	}

	public void run() {
	    // Make sure we have a wall to the left
	    turnLeft();
	    while (!isFacingWall())
	      forward();
	    turnRight();
	   
	    while (!isOverBaggle())
	      stepHandOnWall();
	      
	    pickUpBaggle();
	}
	/* END TEMPLATE */
}

