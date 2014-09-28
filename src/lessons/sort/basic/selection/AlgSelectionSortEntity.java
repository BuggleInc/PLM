package lessons.sort.basic.selection;

import plm.universe.sort.SortingEntity;

public class AlgSelectionSortEntity extends SortingEntity {

	public void run() {
		this.selectionSort();
	}

	/* BEGIN TEMPLATE */
	public void selectionSort() {
		/* BEGIN SOLUTION */
		for (int i = 0; i < getValueCount()-1; i++) {
			int min = i;	
			int j;

			/*  Find the smallest element in the unsorted list */
			for (j = i + 1; j < getValueCount(); j++) {
				if (isSmaller(j,min))
					min = j;				
			}

			/* Swap the smallest unsorted element into the end of the sorted list. */
			swap(min,i);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
