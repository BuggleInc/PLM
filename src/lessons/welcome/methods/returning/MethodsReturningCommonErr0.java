package lessons.welcome.methods.returning;



public class MethodsReturningCommonErr0 extends plm.universe.bugglequest.SimpleBuggle {
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
		for (int i=0; i<7; i++) {
			if (haveBaggle()) 
				return;
			right();
			forward();
			left();
		}
	}
	boolean haveBaggle() {
		boolean hasBaggle = false;
		for (int i=0 ; i < 6 ; i++) {
			forward();
		}
		for (int i = 0 ; i < 6 ; i++) {
			backward();
		}
		return hasBaggle;
	}
}
