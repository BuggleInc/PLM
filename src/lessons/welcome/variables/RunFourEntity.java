package lessons.welcome.variables;

import plm.core.model.Game;

public class RunFourEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument in this exercise."));
	}
	@Override
	public void backward(int i) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument in this exercise."));
	}


	@Override
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		int cpt = 0;
		while (cpt != 4) {
			forward();
			if (isOverBaggle())
				cpt++;
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
