package lessons.welcome.loopfor;

import plm.universe.bugglequest.SimpleBuggle

class LoopCourseForestEntity extends SimpleBuggle {
	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		for (i <- 1 to 7;  side <- 1 to 4){
				for (step <- 1 to 4)
					stepForward();
				left();
				for (step <- 1 to 2)
					stepForward();
				right();
				for (step <- 1 to 4)
					stepForward();
				right();
				stepForward();
				stepForward();
				left();
				for (step <- 1 to 4)
					stepForward();
				left();
		}
		/* END SOLUTION */
	}
	/* END REMOTE */
}
