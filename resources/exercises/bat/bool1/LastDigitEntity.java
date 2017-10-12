package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class LastDigitEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( lastDigit((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
	}

	/* BEGIN TEMPLATE */
	boolean lastDigit(int a, int b) {
		/* BEGIN SOLUTION */
		return a%10 == b%10;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
