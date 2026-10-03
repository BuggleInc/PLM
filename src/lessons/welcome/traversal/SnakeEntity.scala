package lessons.welcome.traversal;

import plm.universe.Direction;
import plm.universe.bugglequest.SimpleBuggle;

class SnakeEntity extends SimpleBuggle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		brushDown();
		while (!endingPosition()) {
			snakeStep();
		}
	}
	def endingPosition():Boolean = {
		if (! isFacingWall()) 
			return false;

		var res = false;
		left();
		if (isFacingWall()) 
			res = true;
		right();		
		return res;
	}

	def snakeStep(): Unit = {
		if (isFacingWall()) {
			if (getDirection() == Direction.EAST) {
				left();
				stepForward();
				left();
			} else {
				right();
				stepForward();
				right();
			}
		} else {
			stepForward();
		}

		/* END SOLUTION */
	}
	/* END TEMPLATE */	
	/* END REMOTE */
}
