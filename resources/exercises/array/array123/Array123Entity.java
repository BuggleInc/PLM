package array.array123;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Array123Entity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( array123((int[])t.getParameter(0)) );
	}

	/* BEGIN TEMPLATE */
	boolean array123(int[] nums) {
		/* BEGIN SOLUTION */
		// Note: iterate < length-2, so can use i+1 and i+2 in the loop
		for (int i=0; i < (nums.length-2); i++) {
			if (nums[i]==1 && nums[i+1]==2 && nums[i+2]==3) return true;
		}
		return false;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
