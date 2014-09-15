package lessons.turmites.langton;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;

class ScalaLangtonEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	def step() {
		/* BEGIN SOLUTION */
		if (getGroundColor() == Color.white) {
			right();

			setBrushColor(Color.black);
			brushDown();
			brushUp();

			forward();
		} else {
			left();

			setBrushColor(Color.white);
			brushDown();
			brushUp();

			forward();				
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run() { 
		val nbSteps = getParam(0).asInstanceOf[Int]; 
		for (i <- 1 to nbSteps) {
			step();
			world.asInstanceOf[lessons.turmites.universe.TurmiteWorld].stepDone();
		}
	}
}
