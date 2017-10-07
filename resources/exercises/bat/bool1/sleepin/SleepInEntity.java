package bat.bool1.sleepin;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class SleepInEntity extends BatEntity {

	public void run(BatTest t) {
		t.setResult( sleepIn((Boolean)t.getParameter(0),(Boolean)t.getParameter(1)) );
	}

	/* BEGIN TEMPLATE */
	boolean sleepIn(boolean weekday, boolean vacation) {
		/* BEGIN SOLUTION */
		return !weekday || vacation;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
