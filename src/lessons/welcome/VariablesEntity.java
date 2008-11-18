package lessons.welcome;

public class VariablesEntity extends universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException("Pas le droit d'utiliser forward(int) dans cet exercice");
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException("Pas le droit d'utiliser backward(int) dans cet exercice");
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
