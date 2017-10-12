package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

class ScalaLastDigit2Entity extends BatEntity {

	/* BEGIN TEMPLATE */
def lastDigit(a:Int, b:Int, c:Int):Boolean = {
		/* BEGIN SOLUTION */
	val da = a % 10
	val db = b % 10
	val dc = c % 10
	return da == db || da == dc || dc == db
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( lastDigit((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
	}

}
