package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;
class PictureMono1Entity extends SimpleBuggle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		for (i <- 1 to 4) {
			makeV();
		}
	}
	def mark(): Unit = {
		brushDown();
		brushUp();
	}

	def makeV(): Unit = {
		forward(2);
		mark();

		step();
		left();
		step();
		mark();

		stepBack();
		right();
		step();
		mark();

		forward(2);
		left();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
