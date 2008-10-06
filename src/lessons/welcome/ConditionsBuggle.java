package lessons.welcome;

import bugglequest.core.SimpleBuggle;

public class ConditionsBuggle extends SimpleBuggle {
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
