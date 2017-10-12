package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

class ScalaStringSplosionEntity extends BatEntity {
	/* BEGIN TEMPLATE */
def stringSplosion(str:String):String = {
		/* BEGIN SOLUTION */
  var res = ""
  for (i <- 0 to str.length-1) 
    res ++= str.substring(0,i+1)
  return res
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( stringSplosion((String)t.getParameter(0)) ); 
	}

}
