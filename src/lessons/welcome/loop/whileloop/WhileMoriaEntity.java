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
		back();
		while (!isFacingWall()) {
			while (!isOverBaggle() && !isFacingWall())
				forward();
			if (isOverBaggle()) {
				pickupBaggle();
				back();
				while (!isOverBaggle())
					forward();
				backward();
				dropBaggle();
				back();
				forward();
			}
		}
		right();
		forward();
		left();
		forward();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
