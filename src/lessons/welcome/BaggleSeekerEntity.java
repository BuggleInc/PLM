package lessons.welcome;

public class BaggleSeekerEntity extends jlm.universe.bugglequest.SimpleBuggle {
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
		/* BEGIN SOLUTION */
		while (!isOverBaggle()) {
		  forward();
		}
		/* END TEMPLATE */
	}
}
