package lessons.sort.basic.cocktail

import plm.universe.sort.SortingEntity;

class AlgCocktailSort1Entity extends SortingEntity {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		cocktailSort();
	}

	/* BEGIN TEMPLATE */
	def cocktailSort(): Unit = {
		/* BEGIN SOLUTION */
		var swapped = true;
		while (swapped) {
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
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */

}

