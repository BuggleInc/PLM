package lessons.sort.basic.bubble;

import plm.universe.sort.SortingEntity;

public class AlgBubbleSort1Entity extends SortingEntity {

	public void run() {
		this.bubbleSort();
	}

	/* BEGIN TEMPLATE */
	public void bubbleSort()  {
		/* BEGIN SOLUTION */
		boolean swapped;
		do {
			swapped = false;
			for (int i=0; i<getValueCount()-1; i++)
				if (!isSmaller(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}
		} while (swapped);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
