/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class RedTicketEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( redTicket((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
	}

	/* BEGIN TEMPLATE */
	int redTicket(int a, int b, int c) {
		/* BEGIN SOLUTION */
		if (a == b && b == c && c == 2)
			return 10;
		else if (a == b && b == c)
			return 5;
		else if (b != a && c != a)
			return 1;
		else
			return 0;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
