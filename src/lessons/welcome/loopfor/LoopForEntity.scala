package lessons.welcome.loopfor;

import plm.universe.bugglequest.SimpleBuggle
import plm.core.model.Game

class ScalaLoopForEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument."));
	}

	override def run() {
		/* BEGIN SOLUTION */
		var cpt = 0
		while (!isOverBaggle()) {
			cpt+=1;
			forward();
		}
		pickupBaggle();
		for (cpt2 <- 0  to cpt-1) {
			backward();
		}
		dropBaggle();
		/* END SOLUTION */
	}
}
