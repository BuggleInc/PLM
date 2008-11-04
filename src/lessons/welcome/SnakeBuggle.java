package lessons.welcome;

import universe.bugglequest.Direction;
import universe.bugglequest.SimpleBuggle;

public class SnakeBuggle extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	boolean endingPosition() {
		if (! isFacingWall()) 
			return false;

		boolean res = false;
		turnLeft();
		if (isFacingWall()) 
			res = true;
		turnRight();		
		return res;
	}

	void snakeStep() {
		if (isFacingWall()) {
			if (getDirection() == Direction.EAST) {
				turnLeft();
				forward();
				turnLeft();
			} else {
				turnRight();
				forward();
				turnRight();
			}
		} else {
			forward();
		}

	}

	@Override
	public void run() {
		brushDown();
		while (!endingPosition()) {
			snakeStep();
		}
	}

	/* END TEMPLATE */	
}
