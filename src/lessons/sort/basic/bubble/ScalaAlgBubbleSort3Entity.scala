package lessons.sort.basic.bubble

import plm.universe.sort.SortingEntity;

class ScalaAlgBubbleSort3Entity extends SortingEntity {

	override def run() {
		bubbleSort3();
	}

	/* BEGIN TEMPLATE */
	def bubbleSort3() {
		/* BEGIN SOLUTION */
		for (i <- getValueCount()-1 to 0 by -1) {
			var swapped = false;
			for (j <- 0 to i-1) {
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

