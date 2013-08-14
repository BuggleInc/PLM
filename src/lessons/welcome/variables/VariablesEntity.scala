package lessons.welcome.variables;

import jlm.core.model.Game

class ScalaVariablesEntity extends jlm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument"));
	}

	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument"));
	}


	override def run() {
		/* BEGIN SOLUTION */
		var stepper = 0;
		while (!isOverBaggle()) {
			stepper += 1
			forward()
		}
		pickupBaggle();
		while (stepper>0) {
			backward()
			stepper -= 1
		}
		dropBaggle();
		/* END SOLUTION */
	}
}
