package lessons.welcome;

public class LoopForEntity extends jlm.universe.bugglequest.SimpleBuggle {
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
		for (int cpt2=0 ; cpt2<cpt ; cpt2++) {
			backward();
		}
		dropBaggle();
		/* END TEMPLATE */
	}
}
