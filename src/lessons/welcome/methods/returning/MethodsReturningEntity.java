package lessons.welcome.methods.returning;

import jlm.core.model.Game;


public class MethodsReturningEntity extends jlm.universe.bugglequest.SimpleBuggle {
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
		for (int i=0; i<7; i++) {
			if (haveBaggle()) 
				return;
			right();
			forward();
			left();
		}
	}
	/* BEGIN TEMPLATE */
	boolean haveBaggle() {
		/* BEGIN SOLUTION */
		boolean res = false;
		for (int i=0; i<6; i++) {
			if (isOverBaggle()) 
				res = true;
			forward();
		}
		for (int i=0; i<6; i++) 
			backward();
		return res;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
