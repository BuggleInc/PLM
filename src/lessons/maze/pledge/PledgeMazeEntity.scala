package lessons.maze.pledge;

import plm.universe.Direction;

class PledgeMazeEntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		var state = 0 ;
		this.angleSum = 0;
		this.setDirection(this.chosenDirection);
		while ( !isOverBaggle() ) {
			state match {
			case 0 => // North runner mode
				while ( !isFacingWall() )
					stepForward();
		
				right(); // make sure that we have a left wall
				angleSum -=1;
				state = 1; // time to enter the Left Follower mode
			case 1 => // Left Follower Mode
				stepHandOnWall(); // follow the left wall
				if ( isChosenDirectionFree() && angleSum == 0  ) 
					state =0; // time to enter in North Runner mode
			case _ =>
			}
		}
		pickupBaggle();
	}

	var angleSum= 0;

	def stepHandOnWall(): Unit = {
		while ( ! isFacingWall() ) {
			stepForward();
			left();
			angleSum += 1;
		}
		right();
		angleSum -= 1;
	}

	val chosenDirection = Direction.NORTH;

	def isChosenDirectionFree(): Boolean = {
		val memorizedD = getDirection();
		setDirection(chosenDirection);
		val isFree = !isFacingWall();
		setDirection(memorizedD);
		return isFree;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
