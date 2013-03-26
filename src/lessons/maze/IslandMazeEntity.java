package lessons.maze;

import jlm.universe.Direction;

public class IslandMazeEntity extends jlm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException("setX(int) forbidden in this exercise");
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException("setY(int) forbidden in this exercise");
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException("setPos(int,int) forbidden in this exercise");
	}

	/* BEGIN SOLUTION */
	 public void run() {
		 int state = 0 ;
		 this.setDirection(this.chosenDirection);
		 while ( !isOverBaggle() )
		 {
			 switch ( state )
			 {
			 case 0: // North runner mode
				 while ( !isFacingWall() )
				 {
					 forward();
			     }
				 this.turnRight(); // make sure that we have a left wall
				 state = 1; // time to enter the Left Follower mode
			 break;
			 case 1: // Left Follower Mode
				 this.stepHandOnWall(); // follow the left wall
		         if ( this.isChosenDirectionFree() && (this.getDirection() == this.chosenDirection)  ) 
		         {
		        	 state =0; // time to enter in North Runner mode
			     }
				          break;
		      }
		 }
		 this.pickUpBaggle();
	 }

	private void stepHandOnWall(){
		while ( ! isFacingWall() )
		{
			forward();
			turnLeft();
		}
		turnRight();
	}

	Direction chosenDirection = Direction.NORTH;

	private boolean isChosenDirectionFree() {
		Direction memorizedD = getDirection();
		boolean isFree = false;
		this.setDirection(this.chosenDirection);
		if (!isFacingWall()) {
			isFree=true;
		} 
		this.setDirection(memorizedD);
		return isFree;
	}
	

	/* END TEMPLATE */
	
}


