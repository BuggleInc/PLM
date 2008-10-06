package lessons.welcome;

import bugglequest.core.Direction;

public class MethodsArgsBuggle extends bugglequest.core.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException("Pas le droit d'utiliser forward(int) dans cet exercice");
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException("Pas le droit d'utiliser backward(int) dans cet exercice");
	}


	@Override
	public void run() { 
		move(getY(),getDirection().equals(Direction.NORTH)); 
	} 

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
	/* END TEMPLATE */
}
