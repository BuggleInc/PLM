package lessons.welcome;

import jlm.universe.Direction;

public class MethodsArgsEntity extends jlm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException("forward(int) forbidden in this exercise");
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException("backward(int) forbidden in this exercise");
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
