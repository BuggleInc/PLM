package lessons.sort.basic.cocktail

import plm.universe.sort.SortingEntity;

class ScalaAlgCocktailSort1Entity extends SortingEntity {

	override def run() {
		cocktailSort();
	}

	/* BEGIN TEMPLATE */
	def cocktailSort() {
		/* BEGIN SOLUTION */
		var swapped = false;
		do {
			swapped = false;
			for (i <- 0 to getValueCount()-2)
				if (!isSmaller(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}	
			for (i <- getValueCount()-2 to 0 by -1)
				if (!isSmaller(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}	
		} while (swapped);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}

