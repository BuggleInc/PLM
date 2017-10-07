package bat.bool1.icyhot;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class IcyHotEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( icyHot((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
	}

	/* BEGIN TEMPLATE */
	boolean icyHot(int temp1, int temp2) {

		/* BEGIN SOLUTION */
		return temp1<0&&temp2>100 || temp1>100&&temp2<0;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
