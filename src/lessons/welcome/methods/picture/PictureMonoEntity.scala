package lessons.welcome.methods.picture;

import java.awt.Color;

import jlm.universe.bugglequest.SimpleBuggle;
class ScalaPictureMonoEntity extends SimpleBuggle {

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	def mark() {
		brushDown();
		brushUp();
	}

	def makeV() {
		forward();
		mark();

		forward();
		left();
		forward();
		mark();

		backward();
		right();
		forward();
		mark();

		forward();
		left();
	}



	override def run() {
		for (i <- 1 to 4) {
			makeV();
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
