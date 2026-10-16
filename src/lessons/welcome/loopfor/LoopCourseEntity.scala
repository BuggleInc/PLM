package lessons.welcome.loopfor;

import java.awt.Color
import plm.universe.bugglequest.SimpleBuggle

class LoopCourseEntity extends SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		for (i <- 1 to 10; side <- 1 to 4) {
			for (k <- 1 to 8) {
				step()
			}
			left()
		} 
		/* END SOLUTION */
	}
	/* END REMOTE */
}
