package array.arraycount9;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class ArrayCount9Entity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(arrayCount9((int[]) t.getParameter(0)));
    }

    /* BEGIN TEMPLATE */
    int arrayCount9(int[] nums) {
        /* BEGIN SOLUTION */
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 9) {
                count++;
            }
        }
        return count;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
