package lessons.welcome.loopdowhile;

import java.awt.Color;

class LoopDoWhileEntity extends plm.universe.bugglequest.SimpleBuggle {
	/* BINDINGS TRANSLATION */
	def estSurBlanc():Boolean = { return isGroundWhite(); }

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		do {
			stepForward();
		} while (!isGroundWhite());
		/* END SOLUTION */
	}
	/* END REMOTE */
}
