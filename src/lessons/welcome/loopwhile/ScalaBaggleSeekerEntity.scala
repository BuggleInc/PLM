package lessons.welcome.loopwhile;

import plm.universe.bugglequest.SimpleBuggle;
import plm.core.model.Game

class ScalaBaggleSeekerEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}

	override def backward(i: Int) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	override def run() { 
		/* BEGIN SOLUTION */
		while (!isOverBaggle()) {
			forward();
		}
		/* END SOLUTION */
	}
}
