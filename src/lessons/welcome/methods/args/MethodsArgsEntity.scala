package lessons.welcome.methods.args;

import plm.universe.Direction
import plm.universe.bugglequest.SimpleBuggle;

class MethodsArgsEntity extends SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = { 
		move(getY(),getDirection() == Direction.NORTH); 
	} 

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	def move(steps: Int, fwd:Boolean): Unit = {
		if (fwd) {
			for (i <- 1 to steps) 
				step()
		} else {
			for (i <- 1 to steps) 
				stepBack()
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */
	/* END REMOTE */
}
