package lessons.sort;

import universe.sort.SortingEntity;

/* BEGIN TEMPLATE */
public class AlgShellSort extends SortingEntity {
	public void run() {
		/* BEGIN SOLUTION */
		int h = 1;
		/* find the largest h value possible */
		while ((h * 3 + 1) < getValueCount()) {
			h = 3 * h + 1;
		}

		/* while h remains larger than 0 */
		while( h > 0 ) {
			/* for each set of elements (there are h sets) */
			for (int i = h - 1; i < getValueCount(); i++) {
				/* pick the last element in the set */
				int value = getValue(i);
				int j = i;
				/* compare the element at B to the one before it in the set
				 * if they are out of order continue this loop, moving
				 * elements "back" to make room for B to be inserted.
				 */
				for( j = i; (j >= h) && (getValue(j-h) > value); j -= h) {
					copy(j-h,j);
				}
				/*
				 *  insert B into the correct place
				 */
				setValue(j, value);
			}
			/* all sets h-sorted, now decrease set size */
			h = h / 3;
		}
		for (int i = 0; i < getValueCount(); i++) 
			sorted(i);
		checkme();
		/* END SOLUTION */
	}
}
/* END TEMPLATE */
