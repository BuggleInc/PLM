package lessons.welcome.conditions;

import plm.universe.bugglequest.SimpleBuggle;

class ConditionsEntity extends SimpleBuggle {
	override def run() { 
		/* BEGIN SOLUTION */
		if (isFacingWall())
			stepBackward();
		else
			stepForward();
		/* END SOLUTION */
	}
}
