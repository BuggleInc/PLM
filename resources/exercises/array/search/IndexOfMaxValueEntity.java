package array.search;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class IndexOfMaxValueEntity extends BatEntity {

    public void run(BatTest t) {
        t.setResult(indexOfMaximum((int[]) t.getParameter(0)));
    }

    /* BEGIN TEMPLATE */
    // computes the index of the maximum of the values contained in tab variable
    public int indexOfMaximum(int[] tab) {
        /* BEGIN SOLUTION */
        int max = Integer.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] > max) { // we are looking for the first occurence
                max = tab[i];
                index = i;
            }
        }
        return index;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}




