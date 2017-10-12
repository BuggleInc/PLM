/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class SquirrelPlayEntity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(squirrelPlay((Integer) t.getParameter(0), (Boolean) t.getParameter(1)));
    }

    /* BEGIN TEMPLATE */
    boolean squirrelPlay(int temp, boolean isSummer) {
        /* BEGIN SOLUTION */
        return (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90));
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
