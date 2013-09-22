package lessons.sort.basic.cocktail;

import plm.universe.sort.SortingEntity;

public class AlgCocktailSort1Entity extends SortingEntity {

	public void run() {
		this.cocktailSort();
	}

	/* BEGIN TEMPLATE */
	public void cocktailSort() {
		/* BEGIN SOLUTION */
		boolean swapped;
		do {
			swapped = false;
			for (int i=0; i<getValueCount()-1; i++)
				if (!isSmaller(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}	
			for (int i=getValueCount()-2; i>=0; i--)
				if (!isSmaller(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}	
		} while (swapped);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}

