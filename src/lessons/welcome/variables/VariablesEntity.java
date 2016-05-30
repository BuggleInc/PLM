package lessons.welcome.variables;

public class VariablesEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
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
		while (cpt>0) {
			backward();
			cpt--;
		}
		dropBaggle();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
