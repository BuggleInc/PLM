package lessons.sort.basic.bubble

import plm.universe.sort.SortingEntity;

class AlgBubbleSort3Entity extends SortingEntity {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		bubbleSort3();
	}

	/* BEGIN TEMPLATE */
	def bubbleSort3(): Unit = {
		/* BEGIN SOLUTION */
		var swapped = true;
		var i = getValueCount()-1;
		while (swapped && i > 0) {
			swapped = false;
			for (j <- 0 until i) {
				if (!isSmaller(j,j+1)) {
					swap(j,j+1);
					swapped = true;
				}
			}
			i -= 1;
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */

}

