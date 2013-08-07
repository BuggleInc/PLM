package lessons.welcome.loopwhile;

import jlm.universe.bugglequest.SimpleBuggle;

public class LoopWhileEntity extends SimpleBuggle {
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
		while (!isFacingWall())
			forward();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
