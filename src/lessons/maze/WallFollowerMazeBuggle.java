package lessons.maze;

public class WallFollowerMazeBuggle extends bugglequest.core.SimpleBuggle {

	/* BEGIN SOLUTION */
	public void run() {
		putHandOnSideWall();
		while (!isOverBaggle()) {
			keepHandOnSideWall();
		}
		pickUpBaggle();
	}

	public void putHandOnSideWall() {
		putHandOnLeftWall();
		// putHandOnRightWall();
	}

	public void keepHandOnSideWall() {
		keepHandOnLeftWall();
		// keepHandOnRightWall();
	}

	private void putHandOnLeftWall() {
		turnLeft();
		while (!isFacingWall()) {
			forward();
		}
		turnRight();
	}

	@SuppressWarnings("unused")
	private void putHandOnRightWall() {
		turnRight();
		while (!isFacingWall()) {
			forward();
		}
		turnLeft();
	}

	private void keepHandOnLeftWall() {
		turnLeft();
		if (!isFacingWall()) {
			forward(); // turn left then forward
		} else {
			turnRight();
			if (!isFacingWall()) {
				forward(); // forward, direction did not change
			} else {
				turnRight();
				if (!isFacingWall()) {
					forward(); // turn right then forward
				} else {
					turnRight(); // turn back then forward
					forward();
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void keepHandOnRightWall() {
		turnRight();
		if (!isFacingWall()) {
			forward(); // turn right then forward
		} else {
			turnLeft();
			if (!isFacingWall()) {
				forward(); // forward, direction did not change
			} else {
				turnLeft();
				if (!isFacingWall()) {
					forward(); // turn left then forward
				} else {
					turnLeft(); // turn back then forward
					forward();
				}
			}
		}
	}

	/* END TEMPLATE */

}
