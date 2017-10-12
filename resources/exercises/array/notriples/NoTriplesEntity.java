package array.notriples;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class NoTriplesEntity extends BatEntity {
    public void run(BatTest t) {
        t.setResult(noTriples((int[]) t.getParameter(0)));
    }

    /* BEGIN TEMPLATE */
    boolean noTriples(int[] nums) {
        /* BEGIN SOLUTION */
        // Iterate < length-2, so can use i+1 and i+2 in the loop.
        // Return false immediately if every seeing a triple.
        for (int i = 0; i < (nums.length - 2); i++) {
            int first = nums[i];
            if (nums[i + 1] == first && nums[i + 2] == first) return false;
        }

        // If we get here ... no triples.
        return true;
		/* END SOLUTION */
    }
	/* END TEMPLATE */
}
