package lessons.welcome.variables;


public class VariablesCommonErr0 extends plm.universe.bugglequest.SimpleBuggle {
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
		if(!isOverBaggle()) {
			while(!isOverBaggle()) {
				forward();
			}
			pickupBaggle();
			backward();
			dropBaggle();
		}
	}
}
