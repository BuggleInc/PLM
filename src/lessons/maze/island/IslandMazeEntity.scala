package lessons.maze.island;

import plm.universe.Direction;

class IslandMazeEntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		var state = 0 ;
		setDirection(chosenDirection);
		while ( !isOverBaggle() ) { 
			state match {
			case 0 => // North runner mode
				while ( !isFacingWall() )
					stepForward();
				
				right(); // make sure that we have a left wall
				state = 1; // time to enter the Left Follower mode
			case 1 => // Left Follower Mode
				stepHandOnWall(); // follow the left wall
				if ( isChosenDirectionFree() && (getDirection() == chosenDirection)  ) 
					state =0; // time to enter in North Runner mode
			case _ =>
			}
		}
		pickupBaggle();
	}

	def stepHandOnWall(): Unit = {
		while ( ! isFacingWall() )
		{
			stepForward();
			left();
		}
		right();
	}

	var chosenDirection = Direction.NORTH;

	def isChosenDirectionFree():Boolean = {
		var memorizedD = getDirection();
		setDirection(chosenDirection);
		var isFree = ! isFacingWall();
		setDirection(memorizedD);
		return isFree;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */

}


