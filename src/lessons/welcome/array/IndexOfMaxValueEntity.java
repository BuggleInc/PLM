package lessons.welcome.array;

import jlm.universe.array.ArrayEntity;
import jlm.universe.array.ArrayWorld;
import lessons.welcome.array.IndexOfMaxValueEntity;

public class IndexOfMaxValueEntity extends ArrayEntity {

	@Override
	public void run() {
		ArrayWorld w = (ArrayWorld) this.getWorld();
		this.result = this.indexOfMaximum(w.getValues());
	}

	/* BEGIN TEMPLATE */
// computes the index of the maximum of the values contained in tab variable
public int indexOfMaximum(int[] tab) {
	/* BEGIN SOLUTION */
	int max = Integer.MIN_VALUE;
	int index = 0;
	for (int i=0; i<tab.length; i++) {
		if (tab[i] >= max) {
			max = tab[i];
			index = i;
		}
	}
	return index;
	/* END SOLUTION */
}

	/* END TEMPLATE */

}
