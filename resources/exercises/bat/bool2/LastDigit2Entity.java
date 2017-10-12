/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class LastDigit2Entity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(lastDigit((Integer) t.getParameter(0), (Integer) t.getParameter(1), (Integer) t.getParameter(2)));
    }

    /* BEGIN TEMPLATE */
    boolean lastDigit(int a, int b, int c) {
        /* BEGIN SOLUTION */
        int da = a % 10;
        int db = b % 10;
        int dc = c % 10;
        return da == db || da == dc || dc == db;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
