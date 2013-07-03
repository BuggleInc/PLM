package lessons.welcome.array.indexof.maxvalue;

import jlm.universe.array.ArrayEntity;

public class IndexOfMaxValueEntity extends ArrayEntity {

	@Override
	public void run() {
		setResult( indexOfMaximum(getValues()) );
	}

	/* BEGIN TEMPLATE */
	// computes the index of the maximum of the values contained in tab variable
	public int indexOfMaximum(int[] tab) {
		/* BEGIN SOLUTION */
		int max = Integer.MIN_VALUE;
		int index = 0;
		for (int i=0; i<tab.length; i++) {
			if (tab[i] > max) { // we are looking for the first occurence
				max = tab[i];
				index = i;
			}
		}
		return index;
		/* END SOLUTION */
	}

	/* END TEMPLATE */

}
