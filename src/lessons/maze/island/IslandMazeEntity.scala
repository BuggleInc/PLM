package lessons.maze.island;

import jlm.universe.Direction;
import jlm.core.model.Game

class ScalaIslandMazeEntity extends jlm.universe.bugglequest.SimpleBuggle {
	override def setX(i: Int)  {
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setX() in this exercise."));
	}
	override def setY(i: Int)  { 
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setY() in this exercise."));
	}
	override def setPos(x: Int, y:Int)  { 
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setPos() in this exercise."));
	}

	/* BEGIN TEMPLATE */
	def run() {
	/* BEGIN SOLUTION */
		var state = 0 ;
		setDirection(chosenDirection);
		while ( !isOverBaggle() ) { 
		  state match {
			case 0 => // North runner mode
				while ( !isFacingWall() )
					forward();
				
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

	def stepHandOnWall(){
		while ( ! isFacingWall() )
		{
			forward();
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
	}
	/* END SOLUTION */
	/* END TEMPLATE */

}


