package lessons.welcome.conditions;

import plm.universe.bugglequest.SimpleBuggle;

class ConditionsEntity extends SimpleBuggle {
	/* BEGIN REMOTE */
	override def run(): Unit = { 
		/* BEGIN SOLUTION */
		if (isFacingWall())
			stepBack();
		else
			step();
		/* END SOLUTION */
	}
	/* END REMOTE */
}
