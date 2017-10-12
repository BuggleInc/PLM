package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ScalaAltPairsEntity extends BatEntity {
	/* BEGIN TEMPLATE */
def altPairs(str:String):String={
		/* BEGIN SOLUTION */
  var res = ""
  for (i <- 0 to (str.length-1) by 4)
    res ++= str.substring(i, Math.min(i+2,str.length))
  return res
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( altPairs((String)t.getParameter(0)) );
	}
}
