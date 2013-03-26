package lessons.welcome.array;

import jlm.universe.array.ArrayEntity;
import jlm.universe.array.ArrayWorld;
import lessons.welcome.array.MaxValueEntity;

public class MaxValueEntity extends ArrayEntity {

	@Override
	public void run() {
		ArrayWorld w = (ArrayWorld) this.getWorld();
		this.result = this.maximum(w.getValues());
	}

	/* BEGIN TEMPLATE */
// computes the maximum of the values contained in tab variable
public int maximum(int[] tab) {
	/* BEGIN SOLUTION */
	int max = Integer.MIN_VALUE;
	for (int i=0; i<tab.length; i++) {
		if (tab[i] >= max) {
			max = tab[i];
		}
	}
	return max;
	/* END SOLUTION */
}

	/* END TEMPLATE */

}
