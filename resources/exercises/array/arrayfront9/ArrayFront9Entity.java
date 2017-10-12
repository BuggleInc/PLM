package array.arrayfront9;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ArrayFront9Entity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( arrayFront9((int[])t.getParameter(0)) );
	}

	/* BEGIN TEMPLATE */
	boolean arrayFront9(int[] nums) {
		/* BEGIN SOLUTION */
		// First figure the end for the loop
		int end = nums.length;
		if (end > 4) end = 4;

		for (int i=0; i<end; i++) {
			if (nums[i] == 9) return true;
		}

		return false;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
