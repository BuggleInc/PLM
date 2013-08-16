package lessons.welcome.methods.args;

import jlm.core.model.Game;
import jlm.universe.Direction;
import jlm.universe.bugglequest.SimpleBuggle;

public class MethodsArgsEntity extends SimpleBuggle {
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
		move(getY(),getDirection().equals(Direction.NORTH)); 
	} 

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	public void move(int nbPas, boolean forward) {
		if (forward) {
			for (int i=0; i<nbPas; i++) 
				forward();
		} else {
			for (int i=0; i<nbPas; i++) 
				backward();
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
