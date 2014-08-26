package lessons.welcome.traversal;

import plm.universe.Direction;
import plm.universe.bugglequest.SimpleBuggle;

class ScalaSnakeEntity extends SimpleBuggle {

	/* BEGIN TEMPLATE */
	override def run() {
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

	def snakeStep() {
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

		/* END SOLUTION */
	}
	/* END TEMPLATE */	
}
