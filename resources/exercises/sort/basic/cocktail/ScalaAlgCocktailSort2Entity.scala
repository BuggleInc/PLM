package sort.basic.cocktail

import plm.universe.sort.SortingEntity;

class ScalaAlgCocktailSort2Entity extends SortingEntity {

	override def run() {
		cocktailSort2();
	}

	/* BEGIN TEMPLATE */
	def cocktailSort2() {
		/* BEGIN SOLUTION */
		var swapped=false;
		var begin=0;
		var end=getValueCount()-2;
		do {
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
		} while (swapped && end-begin>1);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}

