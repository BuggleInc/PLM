package array.array667;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class Array667Entity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(array667((int[]) t.getParameter(0)));
    }

    /* BEGIN TEMPLATE */
    int array667(int[] nums) {
        /* BEGIN SOLUTION */
        int count = 0;
        // Note: iterate to length-1, so can use i+1 in the loop
        for (int i = 0; i < (nums.length - 1); i++) {
            if (nums[i] == 6) {
                if (nums[i + 1] == 6 || nums[i + 1] == 7) {
                    count++;
                }
            }
        }
        return count;
        /* END SOLUTION */
    }
    /* END TEMPLATE */
}
