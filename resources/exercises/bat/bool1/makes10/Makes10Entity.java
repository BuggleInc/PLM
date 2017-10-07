package bat.bool1.makes10;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class Makes10Entity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( makes10((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
	}

	/* BEGIN TEMPLATE */
	boolean makes10(int a, int b) {
		/* BEGIN SOLUTION */
		return a==10||b==10||(a+b)==10;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
