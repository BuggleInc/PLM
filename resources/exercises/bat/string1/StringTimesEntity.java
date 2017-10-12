package bat.string1;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class StringTimesEntity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(stringTimes((String) t.getParameter(0), (Integer) t.getParameter(1)));
    }

    /* BEGIN TEMPLATE */
    String stringTimes(String str, int n) {
        /* BEGIN SOLUTION */
        String result = "";
        for (int i = 0; i < n; i++) {
            result = result + str;  // could use += here
        }
        return result;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
