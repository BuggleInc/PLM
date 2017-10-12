package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ScalaStringBitsEntity extends BatEntity {
	/* BEGIN TEMPLATE */
def stringBits(str:String):String = {
		/* BEGIN SOLUTION */
  var res:String = ""
  for (i <- 0 to str.length-1 by 2)
    res += str.substring(i,i+1)
  res
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( stringBits(t.getParameter(0).asInstanceOf[String]) );
	}
}
