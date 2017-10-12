package bat.string1;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class Last2Entity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(last2((String) t.getParameter(0)));
    }

    /* BEGIN TEMPLATE */
    int last2(String str) {
        /* BEGIN SOLUTION */
        // Screen out too-short string case.
        if (str.length() < 2) return 0;

        String end = str.substring(str.length() - 2);
        // Note: substring() with 1 value goes through the end of the string
        int count = 0;

        // Check each substring length 2 starting at i
        for (int i = 0; i < str.length() - 2; i++) {
            String sub = str.substring(i, i + 2);
            if (sub.equals(end)) {  // Use .equals() with strings
                count++;
            }
        }

        return count;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
