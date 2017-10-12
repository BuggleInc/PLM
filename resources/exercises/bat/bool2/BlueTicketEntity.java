/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class BlueTicketEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( blueTicket((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
	}

	/* BEGIN TEMPLATE */
	int blueTicket(int a, int b, int c) {
		/* BEGIN SOLUTION */
		int ab = a + b;
		int ac = a + c;
		int bc = b + c;

		if (ab == 10 || ac == 10 || bc == 10)
			return 10;
		else if (ab == (bc + 10) || ab == (ac + 10))
			return 5;
		else 
			return 0;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
