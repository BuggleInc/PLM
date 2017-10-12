package array.notriples;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class NoTriplesEntity extends BatEntity {
/* BEGIN TEMPLATE */
def noTriples(nums:Array[Int]): Boolean = {
  /* BEGIN SOLUTION */
  var count=0
  for (i <- 0 to nums.length-3)
    if ( (nums(i) == nums(i+1)) && (nums(i+1) == nums(i+2)) )
      return false
  return true
		/* END SOLUTION */
}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( noTriples((int[])t.getParameter(0)) );
	}
}
