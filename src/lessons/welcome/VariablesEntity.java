package lessons.welcome;

public class VariablesEntity extends jlm.universe.bugglequest.SimpleBuggle {
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
		int cpt = 0;
		while (!isOverBaggle()) {
		  cpt++;
		  forward();
		}
		pickUpBaggle();
		while (cpt>0) {
		  backward();
		  cpt--;
		}
		dropBaggle();
		/* END TEMPLATE */
	}
}
