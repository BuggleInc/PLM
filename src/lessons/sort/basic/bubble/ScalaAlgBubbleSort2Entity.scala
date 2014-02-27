package lessons.sort.basic.bubble

import plm.universe.sort.SortingEntity;

class ScalaAlgBubbleSort2Entity extends SortingEntity {

	override def run() {
		bubbleSort2();
	}

	/* BEGIN TEMPLATE */
	def bubbleSort2() {
		/* BEGIN SOLUTION */
		for (i <- getValueCount()-1 to 0 by -1; j <- 0 to i-1) 
			if (!isSmaller(j,j+1)) 
				swap(j,j+1);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}

