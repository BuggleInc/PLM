package lessons.lander.lvl1_lander_101;

import lessons.lander.universe._;

class Lander101Entity extends LanderEntity {
	/* BEGIN REMOTE */
	override def run(): Unit = {
		while (isFlying()) {
			step()
			simulateStep()
		}
	}

	/* BEGIN TEMPLATE */
	override def step(): Unit = {
		/* BEGIN SOLUTION */
		if (getSpeedY() < -9) {
			setDesiredThrust(4)
		} else {
			setDesiredThrust(3);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
