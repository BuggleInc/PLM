/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class TeenSumEntity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(teenSum((Integer) t.getParameter(0), (Integer) t.getParameter(1)));
    }

    /* BEGIN TEMPLATE */
    int teenSum(int a, int b) {
        /* BEGIN SOLUTION */
        if ((a >= 13 && a <= 19) || (b >= 13 && b <= 19))
            return 19;
        else
            return a + b;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
