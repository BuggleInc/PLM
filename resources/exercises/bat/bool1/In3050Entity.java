package bat.bool1;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class In3050Entity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(in3050((Integer) t.getParameter(0), (Integer) t.getParameter(1)));
    }

    /* BEGIN TEMPLATE */
    boolean in3050(int a, int b) {
        /* BEGIN SOLUTION */
        return (a > 29 && a < 41 && b > 29 && b < 41) || (a > 39 && a < 51 && b > 39 && b < 51);
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
