package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

class ScalaStringYakEntity extends BatEntity {
	/* BEGIN TEMPLATE */
def stringYak(str:String):String = {
		/* BEGIN SOLUTION */
  var res = ""
  var i=0
  while (i<str.length) {
    if (i+2<str.length  && str(i) == 'y' && str(i+2)=='k')
      i += 2
    else
      res += str(i)
    i+=1
  }
  res
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( stringYak(t.getParameter(0).asInstanceOf[String]) );
	}
}
