package lessons.welcome.variables;

import plm.core.model.Game

class ScalaRunFourEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	override def run() {
		/* BEGIN SOLUTION */
		var cpt = 0;
		while (cpt != 4) {
			forward();
			if (isOverBaggle())
				cpt += 1
		}
		/* END SOLUTION */
	}
}
