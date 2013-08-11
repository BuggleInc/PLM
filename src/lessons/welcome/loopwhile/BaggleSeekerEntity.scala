package lessons.welcome.loopwhile;

import jlm.universe.bugglequest.SimpleBuggle;
import jlm.core.model.Game

class ScalaBaggleSeekerEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument."));
	}

	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument."));
	}

	override def run() { 
		/* BEGIN SOLUTION */
		while (!isOverBaggle()) {
			forward();
		}
		/* END SOLUTION */
	}
}
