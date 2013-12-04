package lessons.sort.basic.gnome;

import plm.universe.sort.SortingEntity;

public class AlgGnomeSortEntity extends SortingEntity {

	public void run() {
		this.gnomeSort();
	}

	/* BEGIN TEMPLATE */
	public void gnomeSort() {
		/* BEGIN SOLUTION */
		int i=0;
		while (i<getValueCount()-1) {
			if (isSmaller(i,i+1))
				i++;
			else {
				swap(i,i+1);
				i--;
			}
			if (i==-1)
				i=1; // Remaining at 0 would not mean "move forward" as stated in the mission text
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}

