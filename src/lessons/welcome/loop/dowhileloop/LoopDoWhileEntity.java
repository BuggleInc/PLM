package lessons.welcome.loop.dowhileloop;

import java.awt.Color;

public class LoopDoWhileEntity extends jlm.universe.bugglequest.SimpleBuggle {
	boolean isGroundWhite() { 
		return getGroundColor().equals(Color.white)?true:false;
	}
	@Override
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		do {
			forward();
		} while (!isGroundWhite());
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
