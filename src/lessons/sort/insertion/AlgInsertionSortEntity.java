package lessons.sort.insertion;

import jlm.universe.sort.SortingEntity;

public class AlgInsertionSortEntity extends SortingEntity {
	
	public void run() {
		this.insertionSort();
	}
	
	/* BEGIN TEMPLATE */
	public void insertionSort() {
		/* BEGIN SOLUTION */
		for (int i = 1; i < getValueCount(); i++) {
			int value = getValue(i);
			int j = i;
			while ((j > 0) && (!isSmallerThan(j-1,value))) {
				copy(j-1,j);
				j--;
			}
			setValue(j,value);
		}
		for (int i = 0; i < getValueCount(); i++) 
			sorted(i);
		checkme(); /* color everything in blue */
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	
}

