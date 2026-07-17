package lessons.welcome.methods.returning;

import plm.core.model.Game

class MethodsReturningEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def run() { 
		for (i <- 1 to 7) {
			if (haveBaggle()) 
				return;
			right();
			stepForward();
			left();
		}
	}
	/* BEGIN TEMPLATE */
	def haveBaggle():Boolean = {
		/* BEGIN SOLUTION */
		var res = false
		for (i <- 1 to 6) {
			if (isOverBaggle()) 
				res = true;
			stepForward();
		}
		backward(6);
		return res;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
