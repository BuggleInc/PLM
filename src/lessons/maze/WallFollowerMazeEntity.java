package lessons.maze;

public class WallFollowerMazeEntity extends universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException("setX(int) forbidden in this exercise");
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException("setY(int) forbidden in this exercise");
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException("setPos(int,int) forbidden in this exercise");
	}
/* Solution by Martin
	// Stop moving if stepping over a baggle 
	public void forward() {
	    if (!isOverBaggle()) 
	      super.forward();
	}

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
*/
	/* BEGIN SOLUTION */
	public void run() {
		putHandOnSideWall();
		while (!isOverBaggle()) {
			keepHandOnSideWall();
		}
		pickUpBaggle();
	}

	public void putHandOnSideWall() {
		putHandOnLeftWall();
		// putHandOnRightWall();
	}

	public void keepHandOnSideWall() {
		keepHandOnLeftWall();
		// keepHandOnRightWall();
	}

	private void putHandOnLeftWall() {
		turnLeft();
		while (!isFacingWall()) {
			forward();
		}
		turnRight();
	}

	@SuppressWarnings("unused")
	private void putHandOnRightWall() {
		turnRight();
		while (!isFacingWall()) {
			forward();
		}
		turnLeft();
	}

	private void keepHandOnLeftWall() {
		turnLeft();
		if (!isFacingWall()) {
			forward(); // turn left then forward
		} else {
			turnRight();
			if (!isFacingWall()) {
				forward(); // forward, direction did not change
			} else {
				turnRight();
				if (!isFacingWall()) {
					forward(); // turn right then forward
				} else {
					turnRight(); // turn back then forward
					forward();
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void keepHandOnRightWall() {
		turnRight();
		if (!isFacingWall()) {
			forward(); // turn right then forward
		} else {
			turnLeft();
			if (!isFacingWall()) {
				forward(); // forward, direction did not change
			} else {
				turnLeft();
				if (!isFacingWall()) {
					forward(); // turn left then forward
				} else {
					turnLeft(); // turn back then forward
					forward();
				}
			}
		}
	}

	/* END TEMPLATE */

}
