package lessons.maze.pledge;

import plm.universe.Direction;
import plm.core.model.Game

class ScalaPledgeMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def setX(i: Int)  {
		if (isInited)
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead."));
	}
	override def setY(i: Int)  { 
		if (isInited)
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead."));
	}
	override def setPos(x: Int, y:Int)  { 
		if (isInited)
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead."));
	}

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		var state = 0 ;
		this.angleSum = 0;
		this.setDirection(this.chosenDirection);
		while ( !isOverBaggle() ) {
			state match {
			case 0 => // North runner mode
				while ( !isFacingWall() )
					forward();
		
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

	def stepHandOnWall(){
		while ( ! isFacingWall() ) {
			forward();
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
}
