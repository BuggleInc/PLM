/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class AlarmClockEntity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(alarmClock((Integer) t.getParameter(0), (Boolean) t.getParameter(1)));
    }

    /* BEGIN TEMPLATE */
    String alarmClock(int day, boolean vacation) {
        /* BEGIN SOLUTION */
        if (!vacation) {
            if (day >= 1 && day <= 5)
                return "7:00";
            else
                return "10:00";
        } else {
            if (day >= 1 && day <= 5)
                return "10:00";
            else
                return "off";
        }
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
