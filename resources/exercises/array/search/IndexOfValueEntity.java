package array.search;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class IndexOfValueEntity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(indexOf((int[]) t.getParameter(0), (Integer) t.getParameter(1)));
    }

    /* BEGIN TEMPLATE */
    // computes the index of the first value equals to 'lookingFor' contained in tab variable
    public int indexOf(int[] tab, Integer lookingFor) {
        /* BEGIN SOLUTION */
        for (int i = 0; i < tab.length; i++)
            if (tab[i] == lookingFor)
                return i;

        return -1;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}




