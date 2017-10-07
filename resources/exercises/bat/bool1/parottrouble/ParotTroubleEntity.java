package bat.bool1.parottrouble;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class ParotTroubleEntity extends BatEntity {

	public void run(BatTest t) {
		t.setResult( parotTrouble((Boolean)t.getParameter(0),(Integer)t.getParameter(1)) );		
	}

	/* BEGIN TEMPLATE */
	boolean parotTrouble(boolean talking, int hour) {
		/* BEGIN SOLUTION */
		return (talking && (hour<7||hour>20));	
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
