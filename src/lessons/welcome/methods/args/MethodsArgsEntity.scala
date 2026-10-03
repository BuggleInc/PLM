package lessons.welcome.methods.args;

import plm.universe.Direction
import plm.universe.bugglequest.SimpleBuggle;
import plm.core.model.Game

class MethodsArgsEntity extends SimpleBuggle {
	override def forward(i: Int): Unit = {
		throw new RuntimeException(Game.i18n.tr("I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int): Unit = {
		throw new RuntimeException(Game.i18n.tr("I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}


	/* BEGIN REMOTE */
	override def run(): Unit = { 
		move(getY(),getDirection() == Direction.NORTH); 
	} 

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	def move(steps: Int, fwd:Boolean): Unit = {
		if (fwd) {
			for (i <- 1 to steps) 
				stepForward()
		} else {
			for (i <- 1 to steps) 
				stepBackward()
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */
	/* END REMOTE */
}
