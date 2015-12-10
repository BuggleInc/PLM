package lessons.welcome.methods.basics;

import plm.core.model.Game

class ScalaMethodsEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  {
		throw new RuntimeException(getGame().i18n.tr("I cannot let you use forward with an argument. Use a loop instead."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(getGame().i18n.tr("I cannot let you use backward with an argument. Use a loop instead."));
	}

	override def run() { 
		/* BEGIN TEMPLATE */
		def goAndGet() {
			/* BEGIN SOLUTION */
			var i = 0;
			while (!isOverBaggle()) {
				i += 1;
				forward();
			}
			pickupBaggle();
			while (i>0) {
				backward();
				i -= 1;
			}
			dropBaggle();
			/* END SOLUTION */
		}

		for (i <- 1 to 7) {
			goAndGet();
			right();
			forward();
			left();
		}
		/* END TEMPLATE */
	} 
}
