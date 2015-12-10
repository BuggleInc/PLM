package lessons.lander.lvl1_lander_101;

import lessons.lander.universe._;

class ScalaLander101Entity extends LanderEntity {
	override def step():Unit = {
		/* BEGIN TEMPLATE */
		/* BEGIN SOLUTION */
		if (getSpeedY() < -9) {
			setDesiredThrust(4)
		} else {
			setDesiredThrust(3);
		}
		/* END SOLUTION */
		/* END TEMPLATE */
	}
}
