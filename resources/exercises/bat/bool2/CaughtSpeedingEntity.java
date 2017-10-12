/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class CaughtSpeedingEntity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(caughtSpeeding((Integer) t.getParameter(0), (Boolean) t.getParameter(1)));
    }

    /* BEGIN TEMPLATE */
    int caughtSpeeding(int speed, boolean isBirthday) {
        /* BEGIN SOLUTION */
        if ((isBirthday && speed <= 65) || (speed <= 60))
            return 0;
        else if ((isBirthday && speed <= 85) || (speed <= 80))
            return 1;
        else
            return 2;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
