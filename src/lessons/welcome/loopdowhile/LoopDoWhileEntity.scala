package lessons.welcome.loopdowhile;

import java.awt.Color;

class LoopDoWhileEntity extends plm.universe.bugglequest.SimpleBuggle {
	def isGroundWhite():Boolean = {
	  if (getGroundColor() == Color.white)
	    return true;
	  return false;
	}
	/* BINDINGS TRANSLATION */
	def estSurBlanc():Boolean = { return isGroundWhite(); }

	override def run() {
		/* BEGIN SOLUTION */
		do {
			stepForward();
		} while (!isGroundWhite());
		/* END SOLUTION */
	}
}
