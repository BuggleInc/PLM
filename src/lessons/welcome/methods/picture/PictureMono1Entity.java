package lessons.welcome.methods.picture;

import plm.universe.bugglequest.SimpleBuggle;
public class PictureMono1Entity extends SimpleBuggle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		makeV();
		makeV();
		makeV();
		makeV();
	}
	void mark() {
		brushDown();
		brushUp();
	}

	void makeV() {
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
