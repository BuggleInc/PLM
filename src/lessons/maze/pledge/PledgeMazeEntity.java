package lessons.maze.pledge;

import plm.universe.Direction;

public class PledgeMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead."));
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead."));
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead."));
	}

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		int state = 0 ;
		this.angleSum = 0;
		this.setDirection(this.chosenDirection);
		while ( !isOverBaggle() )
		{
			switch ( state )
			{
			case 0: // North runner mode
				while ( !isFacingWall() )
					forward();
				
				right(); // make sure that we have a left wall
				angleSum--;
				state = 1; // time to enter the Left Follower mode
				break;
			case 1: // Left Follower Mode
				this.stepHandOnWall(); // follow the left wall
				if ( this.isChosenDirectionFree() && this.angleSum == 0  ) 
					state =0; // time to enter in North Runner mode
				
				break;
			}
		}
		pickupBaggle();
	}

	int angleSum;

	private void stepHandOnWall(){
		while ( ! isFacingWall() )
		{
			forward();
			left();
			this.angleSum++;
		}
		right();
		this.angleSum--;
	}

	Direction chosenDirection = Direction.NORTH;

	private boolean isChosenDirectionFree() {
		Direction memorizedD = getDirection();
		this.setDirection(this.chosenDirection);
		boolean isFree = !isFacingWall();
		this.setDirection(memorizedD);
		return isFree;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
