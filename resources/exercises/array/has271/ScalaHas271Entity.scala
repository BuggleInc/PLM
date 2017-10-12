package array.has271;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Has271Entity extends BatEntity {

	/* BEGIN TEMPLATE */
	def has271(nums:Array[Int]): Boolean = {
		/* BEGIN SOLUTION */
  var count=0
  for (i <- 0 to nums.length-2)
    if ((nums(i) + 5 == nums(i+1)) && (Math.abs(nums(i+2)-nums(i)+1)<=2))
      return true
  return false
		/* END SOLUTION */
}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( has271((int[])t.getParameter(0)) );
	}
}
