package lessons.welcome.loopdowhile;

import java.awt.Color;

class LoopDoWhileEntity extends plm.universe.bugglequest.SimpleBuggle {
	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		var more = true;
		while (more) {
			step();
			more = !isGroundWhite();
		}
		/* END SOLUTION */
	}
	/* END REMOTE */
}
