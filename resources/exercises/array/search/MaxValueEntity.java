package array.search;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class MaxValueEntity extends BatEntity {

    public void run(BatTest t) {
        t.setResult(maxValue((int[]) t.getParameter(0)));
    }

    /* BEGIN TEMPLATE */
    // computes the index of the maximum of the values contained in tab variable
    public int maxValue(int[] tab) {
        /* BEGIN SOLUTION */
        int max = tab[0];
        for (int i = 1; i < tab.length; i++)
            if (tab[i] >= max)
                max = tab[i];

        return max;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}




