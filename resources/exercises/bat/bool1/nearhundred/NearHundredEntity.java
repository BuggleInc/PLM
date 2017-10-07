package bat.bool1.nearhundred;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class NearHundredEntity extends BatEntity {

	public void run(BatTest t) {
		t.setResult( nearHundred((Integer)t.getParameter(0)) );		
	}

	/* BEGIN TEMPLATE */
	boolean nearHundred(int n) {
		/* BEGIN SOLUTION */
		return (90<=n && n<=110)||(190<=n&&n<=210);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
