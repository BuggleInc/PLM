package lessons.sort.basic.bubble

import plm.universe.sort.SortingEntity;

class AlgBubbleSort1Entity extends SortingEntity {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		bubbleSort();
	}

	/* BEGIN TEMPLATE */
	def bubbleSort(): Unit = {
		/* BEGIN SOLUTION */
		var swapped= false
		do {
			swapped = false;
			for (i <- 0 to getValueCount()-2)
				if (!isSmaller(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}
		} while (swapped);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */

}
