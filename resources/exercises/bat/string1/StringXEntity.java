package bat.string1;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class StringXEntity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(stringX((String) t.getParameter(0)));
    }

    /* BEGIN TEMPLATE */
    String stringX(String str) {
        /* BEGIN SOLUTION */
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            // Only append the char if it is not the "x" case
            if (!(i > 0 && i < (str.length() - 1) && str.substring(i, i + 1).equals("x"))) {
                result = result + str.substring(i, i + 1); // Could use str.charAt(i) here
            }
        }
        return result;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
