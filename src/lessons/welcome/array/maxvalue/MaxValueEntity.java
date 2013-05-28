package lessons.welcome.array.maxvalue;

import jlm.universe.array.ArrayEntity;

public class MaxValueEntity extends ArrayEntity {

	@Override
	public void run() {
		setResult( maximum(getValues()) );
	}

	/* BEGIN TEMPLATE */
	// computes the maximum of the values contained in tab variable
	public int maximum(int[] tab) {
		/* BEGIN SOLUTION */
		int max = Integer.MIN_VALUE;
		for (int i=0; i<tab.length; i++) {
			if (tab[i] >= max) {
				max = tab[i];
			}
		}
		return max;
		/* END SOLUTION */
	}

	/* END TEMPLATE */

}
