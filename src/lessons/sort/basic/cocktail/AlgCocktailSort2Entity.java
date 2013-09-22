package lessons.sort.basic.cocktail;

import plm.universe.sort.SortingEntity;

public class AlgCocktailSort2Entity extends SortingEntity {

	public void run() {
		this.cocktailSort2();
	}

	/* BEGIN TEMPLATE */
	public void cocktailSort2() {
		/* BEGIN SOLUTION */
		boolean swapped;
		int begin=0;
		int end=getValueCount()-2;
		do {
			swapped = false;
			for (int i=begin; i<=end; i++)
				if (!isSmaller(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}
			end--;
			for (int i=end; i>=begin; i--)
				if (!isSmaller(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}
			begin++;
		} while (swapped && end-begin>1);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}

