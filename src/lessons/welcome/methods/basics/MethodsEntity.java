package lessons.welcome.methods.basics;

public class MethodsEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException("I cannot let you use forward with an argument in this exercise. Use a loop instead.");
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException("I cannot let you use backward with an argument in this exercise. Use a loop instead.");
	}

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	public void goAndGet() {
		int i = 0;
		while (!isOverBaggle()) {
			i++;
			forward();
		}
		pickupBaggle();
		while (i>0) {
			backward();
			i--;
		}
		dropBaggle();
	}
	/* END SOLUTION */
	/* END TEMPLATE */

	@Override
	public void run() { 
		for (int i=0; i<7; i++) {
			goAndGet();
			right();
			forward();
			left();
		}
	} 
}
