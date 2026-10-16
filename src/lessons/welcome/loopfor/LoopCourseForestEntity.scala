package lessons.welcome.loopfor;

import plm.universe.bugglequest.SimpleBuggle

class LoopCourseForestEntity extends SimpleBuggle {
	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		for (i <- 1 to 7;  side <- 1 to 4){
				for (k <- 1 to 4)
					step();
				left();
				for (k <- 1 to 2)
					step();
				right();
				for (k <- 1 to 4)
					step();
				right();
				step();
				step();
				left();
				for (k <- 1 to 4)
					step();
				left();
		}
		/* END SOLUTION */
	}
	/* END REMOTE */
}
