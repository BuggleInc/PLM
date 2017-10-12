package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ScalaStringMatchEntity extends BatEntity {
	/* BEGIN TEMPLATE */
def stringMatch(a:String, b:String):Int = {
		/* BEGIN SOLUTION */
  val l = Math.min( a.length, b.length )
  var count = 0
  for (i <- 0 to l-2)
    if (a.substring(i,i+2) == b.substring(i,i+2))
      count += 1
  count
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( stringMatch(t.getParameter(0).asInstanceOf[String], t.getParameter(1).asInstanceOf[String]) );
	}
}
