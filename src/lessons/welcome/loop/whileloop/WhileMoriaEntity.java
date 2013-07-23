package lessons.welcome.loop.whileloop;

import jlm.universe.bugglequest.SimpleBuggle;

public class WhileMoriaEntity extends SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException("forward(int) forbidden in this exercise");
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException("backward(int) forbidden in this exercise");
	}

	@Override
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		turnBack();
		while (!isFacingWall()) {
			while (!isOverBaggle() && !isFacingWall())
				forward();
			if (isOverBaggle()) {
				pickupBaggle();
				turnBack();
				while (!isOverBaggle())
					forward();
				backward();
				dropBaggle();
				turnBack();
				forward();
			}
		}
		turnRight();
		forward();
		turnLeft();
		forward();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
