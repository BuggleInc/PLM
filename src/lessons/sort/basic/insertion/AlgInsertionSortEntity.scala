package lessons.sort.basic.insertion

import plm.universe.sort.SortingEntity;

class ScalaAlgInsertionSortEntity extends SortingEntity {

	override def run() {
		insertionSort();
	}

	/* BEGIN TEMPLATE */
	def insertionSort() {
		/* BEGIN SOLUTION */
		for (i <- 1 to getValueCount()-1) {
			var value = getValue(i);
			var j = i;
			while ((j > 0) && (!isSmallerThan(j-1,value))) {
				copy(j-1,j);
				j-=1;
			}
			setValue(j,value);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}

