package lessons.welcome.methods.basics;

import plm.core.model.Game

class MethodsEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int): Unit = {
		throw new RuntimeException(Game.i18n.tr("I cannot let you use forward with an argument. Use a loop instead."));
	}
	override def backward(i: Int): Unit = {
		throw new RuntimeException(Game.i18n.tr("I cannot let you use backward with an argument. Use a loop instead."));
	}

	/* BEGIN REMOTE */
	override def run(): Unit = { 
		/* BEGIN TEMPLATE */
		def goAndGet(): Unit = {
			/* BEGIN SOLUTION */
			var i = 0;
			while (!isOverBaggle()) {
				i += 1;
				stepForward();
			}
			pickupBaggle();
			while (i>0) {
				stepBackward();
				i -= 1;
			}
			dropBaggle();
			/* END SOLUTION */
		}

		for (i <- 1 to 7) {
			goAndGet();
			right();
			stepForward();
			left();
		}
		/* END TEMPLATE */
	} 
	/* END REMOTE */
}
