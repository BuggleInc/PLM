package lessons.sort.basic.shell;

import plm.universe.sort.SortingEntity;

/* Variant with precomputed gap:
 *
 *	int [] gap = {1, 4, 10, 23, 57, 132, 301, 701, 1750};
 *	int h = 0;
 *  // find the largest possible gap 
 *	while (gap[h+1] < getValueCount() && h<gap.length) {
 *	  h++;
 *  }
 *  Then, use gap[h] instead of gap, and decrease by h--
 */

public class AlgShellSortEntity extends SortingEntity {

	public void run() {
		shellSort();
	}

	/* BEGIN TEMPLATE */
	public void shellSort()  {
		/* BEGIN SOLUTION */
		int gap = getValueCount()/2;

		/* while h remains larger than 0 */
		while( gap > 0 ) {
			/* for each set of elements (there are h sets) */
			for (int i = gap - 1; i < getValueCount(); i++) {
				/* pick the last element in the set */
				int value = getValue(i);
				int j = i;
				/* compare the element at B to the one before it in the set
				 * if they are out of order continue this loop, moving
				 * elements "back" to make room for B to be inserted.
				 */
				for(; (j >= gap) && (getValue(j-gap) > value); j -= gap) {
					copy(j-gap,j);
				}
				/*  insert B into the correct place */
				setValue(j, value);
			}
			/* all sets gap-sorted, now decrease set size */
			gap = gap / 2;
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}

