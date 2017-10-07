package bat.bool1.in1020;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class In1020Entity extends BatEntity {
	@Override
	public void run(BatTest t) {
		t.setResult( in1020((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
	}

	/* BEGIN TEMPLATE */
	boolean in1020(int a, int b) {
		/* BEGIN SOLUTION */
		return a>9&&a<21 || b>9&&b<21;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
