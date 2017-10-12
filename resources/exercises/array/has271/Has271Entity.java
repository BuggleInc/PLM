package array.has271;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Has271Entity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( has271((int[])t.getParameter(0)) );
	}

	/* BEGIN TEMPLATE */
	boolean has271(int[] nums) {
		/* BEGIN SOLUTION */
		// Iterate < length-2, so can use i+1 and i+2 in the loop.
		// Return true immediately when seeing 271.
		for (int i=0; i < (nums.length-2); i++) {
			int val = nums[i];
			if (nums[i+1] == (val + 5) &&
					Math.abs(nums[i+2] - (val-1)) <= 2)  return true;
		}

		// If we get here ... none found.
		return false;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
