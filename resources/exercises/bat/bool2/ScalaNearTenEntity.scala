/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

class ScalaNearTenEntity extends BatEntity {
	/* BEGIN TEMPLATE */
def nearTen(num:Int):Boolean = {
		/* BEGIN SOLUTION */
  (num % 10) <= 2 || (num % 10) >= 8
		/* END SOLUTION */
}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( nearTen((Integer)t.getParameter(0)) );
	}
}
