package lessons.welcome.array;

import jlm.universe.array.ArrayEntity;
import jlm.universe.array.ArrayWorld;
import lessons.welcome.array.IndexOfValueEntity;

public class IndexOfValueEntity extends ArrayEntity {

	@Override
	public void run() {
		ArrayWorld w = (ArrayWorld) this.getWorld();
		this.result = this.indexOf(w.getValues(), 17);
	}

	/* BEGIN TEMPLATE */
// computes the index of the first value equals to 'lookingFor' contained in tab variable
public int indexOf(int[] tab, int lookingFor) {
	/* BEGIN SOLUTION */
	for (int i=0; i<tab.length; i++) {
		if (tab[i] == lookingFor) {
			return i;
		}
	}
	return -1;
	/* END SOLUTION */
}

	/* END TEMPLATE */

}
