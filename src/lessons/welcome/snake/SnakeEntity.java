package lessons.welcome.snake;

import jlm.universe.Direction;
import jlm.universe.bugglequest.SimpleBuggle;

public class SnakeEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	boolean endingPosition() {
		if (! isFacingWall()) 
			return false;

		boolean res = false;
		left();
		if (isFacingWall()) 
			res = true;
		right();		
		return res;
	}

	void snakeStep() {
		if (isFacingWall()) {
			if (getDirection() == Direction.EAST) {
				left();
				forward();
				left();
			} else {
				right();
				forward();
				right();
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
	/* END SOLUTION */
	/* END TEMPLATE */	
}
