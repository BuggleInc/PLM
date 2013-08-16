package lessons.welcome.methods.args;

import jlm.universe.Direction
import jlm.universe.bugglequest.SimpleBuggle;
import jlm.core.model.Game

class ScalaMethodsArgsEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument."));
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
