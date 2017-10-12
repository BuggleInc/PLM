package bat.bool1;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class Max1020Entity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(max1020((Integer) t.getParameter(0), (Integer) t.getParameter(1)));
    }

    /* BEGIN TEMPLATE */
    int max1020(int a, int b) {
        /* BEGIN SOLUTION */
        int A = a > b ? a : b;
        int B = a > b ? b : a;
        if (A < 21 && A > 9) {
            return A;
        }
        if (B < 21 && B > 9) {
            return B;
        }
        return 0;
        /* END SOLUTION */
    }
    /* END TEMPLATE */
}
