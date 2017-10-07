package bat.bool1.diff21;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class Diff21Entity extends BatEntity {

	public void run(BatTest t) {
		t.setResult( diff21((Integer)t.getParameter(0)) );		
	}

	/* BEGIN TEMPLATE */
	int diff21(int n) {
		/* BEGIN SOLUTION */
		if (n>21)
			return 2*(n-21);
		return 21-n;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
