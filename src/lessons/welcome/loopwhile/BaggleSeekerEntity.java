package lessons.welcome.loopwhile;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

public class BaggleSeekerEntity extends SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument"));
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument"));
	}


	@Override
	public void run() { 
		/* BEGIN TEMPLATE */
		/* BEGIN SOLUTION */
		while (!isOverBaggle()) {
			forward();
		}
		/* END SOLUTION */
		/* END TEMPLATE */
	}
}
