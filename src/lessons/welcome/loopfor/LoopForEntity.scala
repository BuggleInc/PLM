package lessons.welcome.loopfor;

import plm.universe.bugglequest.SimpleBuggle
import plm.core.model.Game

class LoopForEntity extends SimpleBuggle {
	override def forward(i: Int): Unit = {
		throw new UnsupportedOperationException(Game.i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int): Unit = {
		throw new UnsupportedOperationException(Game.i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		var cpt = 0
		while (!isOverBaggle()) {
			cpt+=1;
			stepForward();
		}
		pickupBaggle();
		for (cpt2 <- 0  to cpt-1) {
			stepBackward();
		}
		dropBaggle();
		/* END SOLUTION */
	}
	/* END REMOTE */
}
