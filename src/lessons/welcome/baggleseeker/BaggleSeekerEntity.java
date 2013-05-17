package lessons.welcome.baggleseeker;

import jlm.universe.bugglequest.SimpleBuggle;

public class BaggleSeekerEntity extends SimpleBuggle {
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
		/* BEGIN TEMPLATE */
		/* BEGIN SOLUTION */
		while (!isOverBaggle()) {
		  forward();
		}
		/* END SOLUTION */
		/* END TEMPLATE */
	}
}
