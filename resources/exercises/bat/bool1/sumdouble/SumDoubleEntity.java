package bat.bool1.sumdouble;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class SumDoubleEntity extends BatEntity {
	
	public void run(BatTest t) {
		t.setResult( sumDouble( ((Integer)(t.getParameter(0))),((Integer)(t.getParameter(1)))) );		
	}

	/* BEGIN TEMPLATE */
	int sumDouble(int a, int b) {
		/* BEGIN SOLUTION */
		if (a==b)
			return (a+b)*2;
		return a+b;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
