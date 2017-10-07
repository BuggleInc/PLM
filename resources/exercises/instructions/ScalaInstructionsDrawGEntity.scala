package lessons.welcome.instructions;


class ScalaInstructionsDrawGEntity extends plm.universe.bugglequest.SimpleBuggle {

	override protected def run() {
		/* BEGIN SOLUTION */
		brushDown();
		left();
		forward();
		forward();
		forward();
		forward();
		left();
		forward();
		forward();
		forward();
		forward();
		left();
		forward();
		forward();
		forward();
		forward();
		left();
		forward();
		forward();
		left();
		forward();
		/* back home */
		brushUp();
		right();
		forward(2);
		right();
		forward();
		left();
		/* END SOLUTION */
	}
}
