package lessons.sort.basic.comb;

import plm.universe.sort.SortingEntity;

public class AlgCombSort11Entity extends SortingEntity {

	public void run() {
		this.combSort11();
	}

	/* BEGIN TEMPLATE */
	public void combSort11() {
		/* BEGIN SOLUTION */
		int gap = getValueCount();
		boolean swapped;
		do {
			if (gap>1) {
				gap /= 1.3;
				if (gap == 10 || gap == 9)
					gap = 11;
			}
			swapped = false;
			for (int i=0; i+gap<getValueCount(); i++)
				if (!isSmaller(i,i+gap)) {
					swap(i,i+gap);
					swapped =true;
				}	
		} while (gap>1 || swapped);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}

