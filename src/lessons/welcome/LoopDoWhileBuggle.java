package lessons.welcome;

import java.awt.Color;

public class LoopDoWhileBuggle extends bugglequest.core.SimpleBuggle {
	boolean isGroundWhite() { 
		return getGroundColor().equals(Color.white)?true:false;
	}
	@Override
	public void run() { 
		/* BEGIN SOLUTION */
		do {
			forward();
		} while (!isGroundWhite());
		/* END TEMPLATE */
	}
}
