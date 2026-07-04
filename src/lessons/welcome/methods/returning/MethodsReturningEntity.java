package lessons.welcome.methods.returning;


public class MethodsReturningEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void run() { 
		for (int i=0; i<7; i++) {
			if (haveBaggle()) 
				return;
			right();
			stepForward();
			left();
		}
	}
	/* BEGIN TEMPLATE */
	boolean haveBaggle() {
		/* BEGIN SOLUTION */
		boolean res = false;
		for (int i=0; i<6; i++) {
			if (isOverBaggle()) 
				res = true;
			stepForward();
		}
		backward(6);
		return res;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
