package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ScalaFrontTimesEntity extends BatEntity {
	/* BEGIN TEMPLATE */
def frontTimes(str:String, n:Int):String = {
		/* BEGIN SOLUTION */
  var frontLen = 3
  if (frontLen > str.length)
    frontLen = str.length
  var front = ""
  if (str.length >0)
    front = str.substring(0,frontLen)
  return front * n
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( frontTimes(t.getParameter(0).asInstanceOf[String], t.getParameter(1).asInstanceOf[Int]) );
	}
}
