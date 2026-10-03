package lessons.turmites.langton;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;

class LangtonEntity extends SimpleBuggle {
	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	def step(): Unit = {
		/* BEGIN SOLUTION */
		if (getGroundColor() == Color.white) {
			right();

			setBrushColor(Color.black);
			brushDown();
			brushUp();

			stepForward();
		} else {
			left();

			setBrushColor(Color.white);
			brushDown();
			brushUp();

			stepForward();
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(): Unit = { 
		val nbSteps = getParamInt(0) 
		for (i <- 1 to nbSteps) {
			step();
			stepDone();
		}
	}
	/* END REMOTE */
}
