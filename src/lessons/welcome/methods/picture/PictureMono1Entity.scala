package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;
class PictureMono1Entity extends SimpleBuggle {

	/* BEGIN TEMPLATE */
	override def run() {
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
		forward(2);
		mark();

		stepForward();
		left();
		stepForward();
		mark();

		stepBackward();
		right();
		stepForward();
		mark();

		forward(2);
		left();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
