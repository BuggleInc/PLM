package lessons.welcome;

public class LoopForBuggle extends bugglequest.core.SimpleBuggle {
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
		for (int cpt2=0 ; cpt2<cpt ; cpt2++) {
			backward();
		}
		dropBaggle();
		/* END TEMPLATE */
	}
}
