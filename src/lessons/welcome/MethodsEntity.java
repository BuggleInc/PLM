package lessons.welcome;

public class MethodsEntity extends universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException("Pas le droit d'utiliser forward(int) dans cet exercice");
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException("Pas le droit d'utiliser backward(int) dans cet exercice");
	}

	/* BEGIN SOLUTION */
	public void goAndGet() {
		int i = 0;
		while (!isOverBaggle()) {
			i++;
			forward();
		}
		pickUpBaggle();
		while (i>0) {
			backward();
			i--;
		}
		dropBaggle();
	}
	/* END TEMPLATE */

	@Override
	public void run() { 
		for (int i=0; i<7; i++) {
			goAndGet();
			turnRight();
			forward();
			turnLeft();
		}
	} 
}
