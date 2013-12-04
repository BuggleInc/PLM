package lessons.sort.basic.bubble;

import plm.universe.sort.SortingEntity;

public class AlgBubbleSort3Entity extends SortingEntity {

	public void run() {
		this.bubbleSort3();
	}

	/* BEGIN TEMPLATE */
	public void bubbleSort3() {
		/* BEGIN SOLUTION */
		for (int i = getValueCount()-1; i>=0; i--) {
			boolean swapped = false;
			for (int j = 0; j<i; j++) {
				if (!isSmaller(j,j+1)) {
					swap(j,j+1);
					swapped=true;
				}
			}
			if (!swapped) {
				return;	
			}
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}

