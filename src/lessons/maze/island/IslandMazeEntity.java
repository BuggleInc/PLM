package lessons.maze.island;

import plm.core.model.Game;
import plm.universe.Direction;

public class IslandMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setX() in this exercise."));
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setY() in this exercise."));
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setPos() in this exercise."));
	}
	
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	public void run() {
		int state = 0 ;
		this.setDirection(this.chosenDirection);
		while ( !isOverBaggle() )
		{
			switch ( state ) {
			case 0: // North runner mode
				while ( !isFacingWall() )
					forward();
				
				this.right(); // make sure that we have a left wall
				state = 1; // time to enter the Left Follower mode
				break;
			case 1: // Left Follower Mode
				this.stepHandOnWall(); // follow the left wall
				if ( isChosenDirectionFree() && (getDirection() == chosenDirection)  ) 
					state =0; // time to enter in North Runner mode
				break;
			}
		}
		this.pickupBaggle();
	}

	private void stepHandOnWall(){
		while ( ! isFacingWall() )
		{
			forward();
			left();
		}
		right();
	}

	Direction chosenDirection = Direction.NORTH;

	private boolean isChosenDirectionFree() {
		Direction memorizedD = getDirection();
		this.setDirection(this.chosenDirection);
		boolean isFree = ! isFacingWall();
		this.setDirection(memorizedD);
		return isFree;
	}
	/* END SOLUTION */
	/* END TEMPLATE */

}


