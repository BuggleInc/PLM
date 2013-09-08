package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;
class ScalaPictureMonoEntity extends SimpleBuggle {

	/* BEGIN TEMPLATE */
	def run() {
		/* BEGIN SOLUTION */
		for (i <- 1 to 4) {
			makeV();
		}
	}
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
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
