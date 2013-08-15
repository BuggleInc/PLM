package lessons.welcome.methods.picture;

import jlm.universe.bugglequest.SimpleBuggle;
public class PictureMonoEntity extends SimpleBuggle {

	/* BEGIN SOLUTION */
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
	}



	public void run() {
		makeV();
		makeV();
		makeV();
		makeV();
	}
	/* END SOLUTION */
}
