package lessons.sort.basic.comb;

import plm.universe.sort.SortingEntity;

public class AlgCombSortEntity extends SortingEntity {

	public void run() {
		this.combSort();
	}

	/* BEGIN TEMPLATE */
	public void combSort() {
		/* BEGIN SOLUTION */
		int gap = getValueCount();
		boolean swapped;
		do {
			if (gap>1) 
				gap /= 1.3;
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

