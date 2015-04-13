package lessons.welcome.methods.args;

import plm.universe.Direction
import plm.universe.bugglequest.SimpleBuggle;
import plm.core.model.Game

class ScalaMethodsArgsEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(getGame().i18n.tr("I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(getGame().i18n.tr("I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}


	override def run() { 
		move(getY(),getDirection() == Direction.NORTH); 
	} 

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	def move(steps: Int, fwd:Boolean) {
		if (fwd) {
			for (i <- 1 to steps) 
				forward()
		} else {
			for (i <- 1 to steps) 
				backward()
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
