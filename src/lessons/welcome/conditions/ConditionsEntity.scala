package lessons.welcome.conditions;

import jlm.universe.bugglequest.SimpleBuggle;

class ConditionsEntity extends SimpleBuggle {
	override def run() { 
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
