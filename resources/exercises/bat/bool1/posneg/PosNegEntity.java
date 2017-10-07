package bat.bool1.posneg;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class PosNegEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( posNeg((Integer)t.getParameter(0),(Integer)t.getParameter(1),(Boolean)t.getParameter(2)) );		
	}

	/* BEGIN TEMPLATE */
	boolean posNeg(int a,int b,boolean negative) {
		/* BEGIN SOLUTION */
		if (negative)
			return a<0&&b<0;
		return (a<0&&b>0) || (a>0&&b<0);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
