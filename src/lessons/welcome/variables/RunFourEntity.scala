package lessons.welcome.variables;

import plm.core.model.Game

class RunFourEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int): Unit = {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int): Unit = {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		var cpt = 0;
		while (cpt != 4) {
			stepForward();
			if (isOverBaggle())
				cpt += 1
		}
		/* END SOLUTION */
	}
	/* END REMOTE */
}
