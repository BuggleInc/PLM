package lessons.welcome.instructions;

class InstructionsDrawGEntity extends plm.universe.bugglequest.SimpleBuggle {

	override def run() {
		/* BEGIN SOLUTION */
		brushDown();
		left();
		stepForward();
		stepForward();
		stepForward();
		stepForward();
		left();
		stepForward();
		stepForward();
		stepForward();
		stepForward();
		left();
		stepForward();
		stepForward();
		stepForward();
		stepForward();
		left();
		stepForward();
		stepForward();
		left();
		stepForward();
		/* back home */
		brushUp();
		right();
		forward(2);
		right();
		stepForward();
		left();
		/* END SOLUTION */
	}
}
