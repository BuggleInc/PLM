package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

class ScalaStringTimesEntity extends BatEntity {
	/* BEGIN TEMPLATE */
def stringTimes(str:String, n:Int):String = {
		/* BEGIN SOLUTION */
  var res = ""
  for (i <- 1 to n)
    res ++= str
  res
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( stringTimes((String)t.getParameter(0), (Integer)t.getParameter(1)) );
	}
}
