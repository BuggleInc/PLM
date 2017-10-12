package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

class RedTicketEntity extends BatEntity {
	/* BEGIN TEMPLATE */
def redTicket(a:Int, b:Int, c:Int):Int = {
		/* BEGIN SOLUTION */
	if (a == b && b == c && c == 2)
		return 10
	else if (a == b && b == c)
		return 5
	else if (b != a && c != a)
		return 1
	else
		return 0
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( redTicket((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
	}

}
