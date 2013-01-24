package lessons.sort.miscellaneous;

import jlm.universe.sort.SortingEntity;

public class AlgGnomeSortEntity extends SortingEntity {
	
	public void run() {
		this.gnomeSort();
	}
	
	/* BEGIN TEMPLATE */
	public void gnomeSort() {
		/* BEGIN SOLUTION */
		int i=0;
		while (i<getValueCount()-1) {
			if (isSmaller(i,i+1))
				i++;
			else {
				swap(i,i+1);
				i--;
			}
			if (i==-1)
				i=0;
		}
		checkme(); /* color everything in blue */
	/* END SOLUTION */
	}
	/* END TEMPLATE */
	
}

