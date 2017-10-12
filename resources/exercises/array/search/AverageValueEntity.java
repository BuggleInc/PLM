package array.search;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class AverageValueEntity extends BatEntity {

    public void run(BatTest t) {
        t.setResult(averageValue((int[]) t.getParameter(0)));
    }

    /* BEGIN TEMPLATE */
    int averageValue(int[] nums) {
        /* BEGIN SOLUTION */
        int total = 0;
        for (int i = 0; i < nums.length; i++)
            total += nums[i];
        return total / nums.length;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}

