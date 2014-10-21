package lessons.sort.basic.selection

import plm.universe.sort.SortingEntity;

class ScalaAlgSelectionSortEntity extends SortingEntity {

	override def run() {
		selectionSort();
	}

	/* BEGIN TEMPLATE */
	def selectionSort() {
		/* BEGIN SOLUTION */
		for (i <- 0 to getValueCount()-2) {
			var min = i;	

			/*  Find the smallest element in the unsorted list */
			for (j <- i + 1 to getValueCount()-1) {
				if (isSmaller(j,min))
					min = j;				
			}

			/* Swap the smallest unsorted element into the end of the sorted list. */
			swap(min,i);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
