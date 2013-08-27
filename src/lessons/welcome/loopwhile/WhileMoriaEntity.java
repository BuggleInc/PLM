package lessons.welcome.loopwhile;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

public class WhileMoriaEntity extends SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument"));
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument"));
	}

	@Override
	/* BEGIN TEMPLATE */
	public void run() { 
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
	/* END TEMPLATE */
}
