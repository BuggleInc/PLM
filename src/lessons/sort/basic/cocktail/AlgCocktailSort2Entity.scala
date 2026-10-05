package lessons.sort.basic.cocktail

import plm.universe.sort.SortingEntity;

class AlgCocktailSort2Entity extends SortingEntity {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		cocktailSort2();
	}

	/* BEGIN TEMPLATE */
	def cocktailSort2(): Unit = {
		/* BEGIN SOLUTION */
		var swapped = true;
		var begin=0;
		var end=getValueCount()-2;
		while (swapped && end-begin>1) {
			swapped = false;
			for (i <- begin to end)
				if (!isSmaller(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}
			end-=1;
			for (i <- end to begin by -1)
				if (!isSmaller(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}
			begin+=1;
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */

}

