package lessons.welcome;

import jlm.universe.bugglequest.SimpleBuggle;

public class BasicsEntity extends SimpleBuggle {

	@Override
	public void run() {
		/* BEGIN TEMPLATE */
		/* BEGIN SOLUTION */
		brushDown();
		for (int i=0;i<4;i++) {
			forward(2);
			turnRight();
		}
		brushUp();
		forward();
		turnRight();
		forward();
		turnLeft();
		/* END SOLUTION */
		/* END TEMPLATE */
	}

}
