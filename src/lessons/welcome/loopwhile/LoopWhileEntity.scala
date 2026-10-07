package lessons.welcome.loopwhile;

import plm.universe.bugglequest.SimpleBuggle;

class LoopWhileEntity extends SimpleBuggle {


	/* BEGIN REMOTE */
	override def run(): Unit = { 
		/* BEGIN SOLUTION */
		while (!isFacingWall())
			stepForward();
		/* END SOLUTION */
	}
	/* END REMOTE */
}
