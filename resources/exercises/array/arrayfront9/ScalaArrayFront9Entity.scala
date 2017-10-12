package array.arrayfront9;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ScalaArrayFront9Entity extends BatEntity {

/* BEGIN TEMPLATE */
def arrayFront9(nums:Array[Int]): Boolean = {
  /* BEGIN SOLUTION */
  for (i <- 0 to Math.min(nums.length,4)-1)
    if (nums(i) == 9)
      return true
  return false
  /* END SOLUTION */
}
/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( arrayFront9((int[])t.getParameter(0)) );
	}
}
