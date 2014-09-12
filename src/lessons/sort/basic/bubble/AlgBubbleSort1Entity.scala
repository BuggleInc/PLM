package lessons.sort.basic.bubble

import plm.universe.sort.SortingEntity;

class ScalaAlgBubbleSort1Entity extends SortingEntity {

	override def run() {
		bubbleSort();
	}

	/* BEGIN TEMPLATE */
	def bubbleSort()  {
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

}
