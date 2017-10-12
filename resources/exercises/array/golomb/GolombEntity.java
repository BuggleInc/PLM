package array.golomb;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class GolombEntity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(golomb((int) t.getParameter(0)));
    }

    /* BEGIN TEMPLATE */
    int golomb(int num) {
        /* BEGIN SOLUTION */
        if (num == 1) {
            return 1;
        } else {
            return 1 + golomb(num - golomb(golomb(num - 1)));
        }
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}

