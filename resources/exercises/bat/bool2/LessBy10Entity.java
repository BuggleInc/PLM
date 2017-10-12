/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class LessBy10Entity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(lessBy10((Integer) t.getParameter(0), (Integer) t.getParameter(1), (Integer) t.getParameter(2)));
    }

    /* BEGIN TEMPLATE */
    boolean lessBy10(int a, int b, int c) {
        /* BEGIN SOLUTION */
        return ((a - b) >= 10) || ((b - a) >= 10) || ((b - c) >= 10) || ((c - b) >= 10) || ((a - c) >= 10) || ((c - a) >= 10);
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
