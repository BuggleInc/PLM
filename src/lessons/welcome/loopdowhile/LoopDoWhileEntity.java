package lessons.welcome.loopdowhile;

import java.awt.Color;

public class LoopDoWhileEntity extends plm.universe.bugglequest.SimpleBuggle {
	boolean isGroundWhite() { 
		return getGroundColor().equals(Color.white)?true:false;
	}
	/* BINDINGS TRANSLATION */
	boolean estSurBlanc() { return isGroundWhite() ; }
	
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
