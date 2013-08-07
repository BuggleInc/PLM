package lessons.welcome.loopfor;

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
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		int cpt = 0;
		while (!isOverBaggle()) {
			cpt++;
			forward();
		}
		pickupBaggle();
		for (int cpt2=0 ; cpt2<cpt ; cpt2++) {
			backward();
		}
		dropBaggle();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
