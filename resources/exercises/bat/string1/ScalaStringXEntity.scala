package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringX extends ExerciseTemplated {
	/* BEGIN TEMPLATE */
def stringX(str:String):String = {
/* BEGIN SOLUTION */
  var res = ""
  for (i <- 0 to str.length-1)
    if (str(i) != 'x' || i == 0 || i == str.length-1)
      res += str.substring(i,i+1)
  res
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( stringX(t.getParameter(0).asInstanceOf[String]) );
	}

}
