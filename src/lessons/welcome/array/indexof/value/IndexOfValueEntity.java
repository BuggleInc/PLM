package lessons.welcome.array.indexof.value;

import jlm.universe.array.ArrayEntity;

public class IndexOfValueEntity extends ArrayEntity {

	@Override
	public void run() {
		setResult( indexOf(getValues(), 17) );
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
