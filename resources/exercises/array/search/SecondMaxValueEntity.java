package array.search;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class SecondMaxValueEntity extends BatEntity {

	public void run(BatTest t) {
		t.setResult( max2Value( (int[])t.getParameter(0) ));
	}

	/* BEGIN TEMPLATE */
	// computes the index of the second maximum of the values contained in tab variable
	int max2Value(int[] tab) {
		/* BEGIN SOLUTION */
		int max = Integer.MIN_VALUE;
		int sec = Integer.MIN_VALUE;
		for (int i=0; i<tab.length; i++) 
			if (tab[i] > max) {
				sec = max;
				max = tab[i];
			} else if (tab[i] > sec) {
				sec = tab[i];
			}
				
		return sec;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}




