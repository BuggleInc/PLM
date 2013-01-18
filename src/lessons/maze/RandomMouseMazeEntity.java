package lessons.maze;

public class RandomMouseMazeEntity extends jlm.universe.bugglequest.SimpleBuggle {
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

	/* BEGIN TEMPLATE */ 
	public void run() {
		while (!isOverBaggle()) {
			if (atAJunction()) {
				takeRandomDirection();
			} else {
				if (!isFacingWall()) {
					forward();
				} else {
					turnRandomly();
				}
			}
		}
		pickUpBaggle();
	}

	/* BEGIN SOLUTION */ 
	public void turnRandomly() {
		switch (random2()) {
		case 0:
			turnLeft();
			break;
		case 1:
			turnRight();
			break;
		}
	}

	public void takeRandomDirection() {
		if (isFacingWall()) {
			turnRandomly();
		} else {
			switch (random3()) {
			case 0:
				turnLeft();
				break;
			case 1:
				forward();
				break;
			case 2:
				turnRight();
				break;
			}
		}
	}

	public boolean atAJunction() {
		boolean junction = false;

		// check left
		turnLeft();
		if (!isFacingWall()) {
			junction = true;
		}
		turnRight();

		// can we skip next check?
		if (junction) {
			return true;
		} else {
			// check right
			turnRight();
			if (!isFacingWall()) {
				junction = true;
			}
			turnLeft();
		}
		return junction;
	}

	/* END TEMPLATE */

	public int random3() {
		double n = Math.random();
		if (n < 0.33) {
			return 0;
		} else if (n < 0.66) {
			return 1;
		} else {
			return 2;
		}
	}

	public int random2() {
		double n = Math.random();
		if (n < 0.5) {
			return 0;
		} else {
			return 1;
		}
	}

}
