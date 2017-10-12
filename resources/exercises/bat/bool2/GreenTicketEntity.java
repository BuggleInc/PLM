/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class GreenTicketEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( greenTicket((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
	}

	/* BEGIN TEMPLATE */
	int greenTicket(int a, int b, int c) {
		/* BEGIN SOLUTION */
		if (a == b && b == c)
			return 20;
		else if (a == b || b == c || a == c)
			return 10;
		else  // (a != b && b != a && c != a)
			return 0;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
