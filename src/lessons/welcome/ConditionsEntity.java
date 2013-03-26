package lessons.welcome;

import jlm.universe.bugglequest.SimpleBuggle;

public class ConditionsEntity extends SimpleBuggle {
	@Override
	public void run() { 
		/* BEGIN TEMPLATE */
		/* BEGIN SOLUTION */
		if (isFacingWall())
			backward();
		else
			forward();
		/* END SOLUTION */
		/* END TEMPLATE */
	}
}
