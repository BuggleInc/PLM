package lessons.welcome;


public class MethodsReturningEntity extends jlm.universe.bugglequest.SimpleBuggle {
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
		for (int i=0; i<7; i++) {
			if (haveBaggle()) 
				return;
			turnRight();
			forward();
			turnLeft();
		}
	}
	/* BEGIN TEMPLATE */
	public boolean haveBaggle() {
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
