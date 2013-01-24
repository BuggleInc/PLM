package lessons.sort.bubble;

import jlm.universe.sort.SortingEntity;

public class AlgBubbleSort2Entity extends SortingEntity {
	
	public void run() {
		this.bubbleSort2();
	}
	
	/* BEGIN TEMPLATE */
	public void bubbleSort2() {
		/* BEGIN SOLUTION */
		for (int i = getValueCount()-1; i>=0; i--) {
			for (int j = 0; j<i; j++) 
				if (!isSmaller(j,j+1)) 
					swap(j,j+1);
			sorted(i);
		}
		checkme(); /* color everything in blue */
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}

