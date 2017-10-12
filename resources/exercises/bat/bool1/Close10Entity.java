package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Close10Entity extends BatEntity {
	public void run(BatTest t) {
		t.setResult(close10((Integer)t.getParameter(0),(Integer)t.getParameter(1)));
	}

	/* BEGIN TEMPLATE */
	int close10(int a, int b) {
		/* BEGIN SOLUTION */
		if (Math.abs(a-10)==Math.abs(b-10))
			return 0;
		if (Math.abs(a-10)<Math.abs(b-10))
			return a;
		return b;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
