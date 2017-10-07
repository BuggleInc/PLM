package bat.bool1.monkeytrouble;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class MonkeyTroubleEntity extends BatEntity {

	public void run(BatTest t) {
		t.setResult( monkeyTrouble((Boolean)t.getParameter(0),(Boolean)t.getParameter(1)) );		
	}
	
	/* BEGIN TEMPLATE */
	public boolean monkeyTrouble(boolean aSmile, boolean bSmile) {
		/* BEGIN SOLUTION */
		if (aSmile && bSmile) {
			return true;
		}
		if (!aSmile && !bSmile) {
			return true;
		}
		return false;
		// This all can be shortened to just:
		// return ((aSmile && bSmile) || (!aSmile && !bSmile));
		/* END SOLUTION */		  
	}	
	/* END TEMPLATE */
}
