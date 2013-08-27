package lessons.welcome.variables;

import plm.core.model.Game

class ScalaRunFourEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument."));
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
