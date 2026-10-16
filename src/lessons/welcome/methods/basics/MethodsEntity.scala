package lessons.welcome.methods.basics;

import plm.core.model.Game

class MethodsEntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = { 
		/* BEGIN TEMPLATE */
		def goAndGet(): Unit = {
			/* BEGIN SOLUTION */
			var i = 0;
			while (!isOverBaggle()) {
				i += 1;
				step();
			}
			pickupBaggle();
			while (i>0) {
				stepBack();
				i -= 1;
			}
			dropBaggle();
			/* END SOLUTION */
		}

		for (i <- 1 to 7) {
			goAndGet();
			right();
			step();
			left();
		}
		/* END TEMPLATE */
	} 
	/* END REMOTE */
}
