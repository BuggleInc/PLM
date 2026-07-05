package lessons.welcome.summative;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

class ScalaMoriaEntity extends SimpleBuggle {
	override def forward(i: Int)  {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	override def run() {
		/* BEGIN SOLUTION */
		back();
		while (!isFacingWall()) {
			while (!isOverBaggle() && !isFacingWall())
				stepForward();
			if (isOverBaggle()) {
				pickupBaggle();
				back();
				while (!isOverBaggle())
					stepForward();
				stepBackward();
				dropBaggle();
				back();
				stepForward();
			}
		}
		right();
		stepForward();
		left();
		stepForward();
		/* END SOLUTION */
	}
}
