package lessons.sort.basic.comb

import plm.universe.sort.SortingEntity;

class ScalaAlgCombSort11Entity extends SortingEntity {

	override def run() {
		combSort11();
	}

	/* BEGIN TEMPLATE */
	def combSort11() {
		/* BEGIN SOLUTION */
		var gap = getValueCount();
		var swapped=false;
		do {
			if (gap>1) {
				gap = (gap.asInstanceOf[Double] / 1.3).asInstanceOf[Int];
				if (gap == 10 || gap == 9)
					gap = 11;
			}
			swapped = false;
			for (i <- 0 to getValueCount()-gap-1)
				if (!isSmaller(i,i+gap)) {
					swap(i,i+gap);
					swapped =true;
				}	
		} while (gap>1 || swapped);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}

