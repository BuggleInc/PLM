package lessons.welcome.conditions;

import plm.universe.bugglequest.SimpleBuggle;

class ScalaConditionsEntity extends SimpleBuggle {
	override def run() { 
		/* BEGIN SOLUTION */
		if (isFacingWall())
			backward();
		else
			forward();
		/* END SOLUTION */
	}
}
