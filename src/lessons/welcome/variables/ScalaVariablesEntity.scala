package lessons.welcome.variables;

import plm.core.model.Game

class ScalaVariablesEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}

	override def backward(i: Int) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
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
