package lessons.welcome.loopwhile;

import plm.universe.bugglequest.SimpleBuggle;

public class BaggleSeekerEntity extends SimpleBuggle {
	
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
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
