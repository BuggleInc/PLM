/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class DateFashionEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( dateFashion((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
	}

	/* BEGIN TEMPLATE */
	int dateFashion(int you, int date) {
		/* BEGIN SOLUTION */
		if (you <= 2 || date <= 2)
			return 0;
		else if (you >= 8 || date >= 8)
			return 2;
		else
			return 1;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
