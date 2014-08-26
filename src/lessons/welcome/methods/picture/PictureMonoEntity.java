package lessons.welcome.methods.picture;

import plm.universe.bugglequest.SimpleBuggle;
public class PictureMonoEntity extends SimpleBuggle {

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
