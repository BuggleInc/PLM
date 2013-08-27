package lessons.welcome.loopwhile;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

class ScalaWhileMoriaEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument"));
	}
	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument"));
	}

	override def run() {
		/* BEGIN SOLUTION */
		back();
		while (!isFacingWall()) {
			while (!isOverBaggle() && !isFacingWall())
				forward();
			if (isOverBaggle()) {
				pickupBaggle();
				back();
				while (!isOverBaggle())
					forward();
				backward();
				dropBaggle();
				back();
				forward();
			}
		}
		right();
		forward();
		left();
		forward();
		/* END SOLUTION */
	}
}
