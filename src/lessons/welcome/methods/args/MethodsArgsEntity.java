package lessons.welcome.methods.args;

import plm.universe.Direction;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsArgsEntity extends SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(getGame().i18n.tr("I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	@Override
	public void backward(int i) {
		throw new RuntimeException(getGame().i18n.tr("I cannot let you use backward with an argument in this exercise. Use a loop instead."));
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
