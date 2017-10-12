package bat.string1;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class FrontTimesEntity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(frontTimes((String) t.getParameter(0), (Integer) t.getParameter(1)));
    }

    /* BEGIN TEMPLATE */
    String frontTimes(String str, int n) {
        /* BEGIN SOLUTION */
        int frontLen = 3;
        if (frontLen > str.length()) {
            frontLen = str.length();
        }
        String front = str.substring(0, frontLen);

        String result = "";
        for (int i = 0; i < n; i++) {
            result = result + front;
        }
        return result;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
