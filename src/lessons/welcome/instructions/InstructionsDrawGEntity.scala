package lessons.welcome.instructions;

class InstructionsDrawGEntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		brushDown();
		left();
		step();
		step();
		step();
		step();
		left();
		step();
		step();
		step();
		step();
		left();
		step();
		step();
		step();
		step();
		left();
		step();
		step();
		left();
		step();
		/* back home */
		brushUp();
		right();
		forward(2);
		right();
		step();
		left();
		/* END SOLUTION */
	}
	/* END REMOTE */
}
