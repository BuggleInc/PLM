package lessons.welcome.variables;


public class VariablesCommonErr4 extends plm.universe.bugglequest.SimpleBuggle {
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
		int nbForward = 0;
		int nbBackward = 0;
		while(!isOverBaggle()) {
			nbForward++;
			forward();
		}
		pickupBaggle();
		while(nbBackward != nbForward) {
			backward();
			nbBackward++;
		}
	}
}