package array.array123;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Array123Entity extends BatEntity {
	/* BEGIN TEMPLATE */
def array123(nums:Array[Int]): Boolean = {
		/* BEGIN SOLUTION */
  for (i <- 0 to nums.length-3)
    if (nums(i)==1  &&  nums(i+1)==2  &&  nums(i+2)==3)
      return true
      /* END SOLUTION */
  return false
}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( array123((int[])t.getParameter(0)) );
	}
}
