package lessons.maze;

import jlm.universe.Direction;

public class PledgeMazeEntity extends jlm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException("setX(int) forbidden in this exercise");
		super.setX(i);
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException("setY(int) forbidden in this exercise");
		super.setY(i);
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException("setPos(int,int) forbidden in this exercise");
		super.setPos(i,j);
	}

	/* BEGIN SOLUTION */
	public void run() {
		chosenD = Direction.NORTH;
		setDirection(chosenD);

		while (!isOverBaggle()) {
			while (!isFacingWall()) {
				forward();
			}
			turnLeft();

			do {
				keepHandOnSideWall();
			} while (!(angleSum == 0 && isChosenDirectionFree()) && !isOverBaggle());
		}
		
		pickUpBaggle();
	}

	int angleSum = 0;
	Direction chosenD;
	Direction memorizedD;

	private boolean isChosenDirectionFree() {
		memorizedD = getDirection();
		setDirection(chosenD);
		if (!isFacingWall()) {
			return true;
		} else {
			setDirection(memorizedD);
			return false;
		}
	}

	public void keepHandOnSideWall() {
		keepHandOnRightWall();
	}
	
	private void keepHandOnRightWall() {
		turnRight();
		if (!isFacingWall()) {
			angleSum = angleSum + 1;
			forward(); // turn right then forward
		} else {
			turnLeft();
			if (!isFacingWall()) {
				forward(); // forward, direction did not change
			} else {
				turnLeft();
				if (!isFacingWall()) {
					angleSum = angleSum - 1;
					forward(); // turn left then forward
				} else {
					angleSum = -2;
					turnLeft(); // turn back then forward
					forward();
				}
			}
		}
	}

	/* END TEMPLATE */
}
