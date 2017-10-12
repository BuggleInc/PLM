package array.search;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class ExtremaEntity extends BatEntity {

    public void run(BatTest t) {
        t.setResult(extrema((int[]) t.getParameter(0)));
    }

    /* BEGIN TEMPLATE */
    int extrema(int[] nums) {
        /* BEGIN SOLUTION */
        if (nums.length > 0) {
            int min = nums[0];
            int max = nums[0];
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] < min) {
                    min = nums[i];
                }
                if (nums[i] > max) {
                    max = nums[i];
                }
            }
            return max - min;
        } else {
            return 0;
        }
		
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}

