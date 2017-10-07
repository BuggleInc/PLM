package lessons.welcome.conditions;

import plm.universe.bugglequest.SimpleBuggle;

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
