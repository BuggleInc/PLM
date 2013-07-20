package lessons.welcome.variables;

public class RunFourEntity extends jlm.universe.bugglequest.SimpleBuggle {
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
		while (cpt != 4) {
			forward();
			if (isOverBaggle())
				cpt++;
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
