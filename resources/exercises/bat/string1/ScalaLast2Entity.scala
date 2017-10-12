package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

class ScalaLast2Entity extends BatEntity {
	/* BEGIN TEMPLATE */
def last2(str:String):Int = {
		/* BEGIN SOLUTION */
  val l = str.length
  if (l < 2)
    0
  val end = str.substring(l-2,l)
  var count = 0
  for (i <- 0 to str.length-3)
    if (str.substring(i,i+2) == end)
      count += 1
  count
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( last2(t.getParameter(0).asInstanceOf[String]) );
	}

}
