package lessons.welcome.array.occurenceofvalue;

import jlm.universe.array.ArrayEntity;

public class OccurrenceOfValueEntity extends ArrayEntity {

	@Override
	public void run() {
		setResult( occurrences(getValues(), 17) );
	}

	/* BEGIN TEMPLATE */
	// computes the occurrences of the value 'lookingFor' contained in tab variable
	public int occurrences(int[] tab, int lookingFor) {
		/* BEGIN SOLUTION */
		int count = 0;
		for (int i=0; i<tab.length; i++) {
			if (tab[i] == lookingFor) {
				count++;
			}
		}
		return count;
		/* END SOLUTION */
	}

	/* END TEMPLATE */

}
