package lessons.welcome.variables;


public class VariablesCommonErr2 extends plm.universe.bugglequest.SimpleBuggle {
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
		int nbSteps = 0;
		while(!isOverBaggle()) {
			forward();
			nbSteps++;
		}
		int alreadyDone = 0;
		while(alreadyDone != nbSteps) {
			backward();
			alreadyDone++;
		}
	}
}
