package lessons.welcome.summative;

import plm.universe.bugglequest.SimpleBuggle;

public class MoriaCommonErr0 extends SimpleBuggle {
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
		back();
		while(!isFacingWall()) {
			while(!isOverBaggle()) {
				forward();
			}
			pickupBaggle();
		}
	}
}
