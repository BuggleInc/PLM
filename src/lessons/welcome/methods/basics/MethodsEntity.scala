package lessons.welcome.methods.basics;

import plm.core.model.Game

class ScalaMethodsEntity extends plm.universe.bugglequest.SimpleBuggle {
  	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument."));
	}

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
	/* END TEMPLATE */

	override def run() { 
		for (i <- 1 to 7) {
			goAndGet();
			right();
			forward();
			left();
		}
	} 
}
