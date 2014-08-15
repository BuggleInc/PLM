//RemoteSort

/* BEGIN TEMPLATE */
void shellSort()  {
	/* BEGIN SOLUTION */
	int count=getValueCount();
	int gap = count/2;
	/* while h remains larger than 0 */
	while( gap > 0 ) {
		/* for each set of elements (there are h sets) */
		int i;
		for (i = gap - 1; i < count; i++) {
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



void run(){
	shellSort();
}
