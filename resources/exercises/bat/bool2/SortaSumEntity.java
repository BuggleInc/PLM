/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SortaSumEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( sortaSum((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
	}

	/* BEGIN TEMPLATE */
	int sortaSum(int a, int b) {
		/* BEGIN SOLUTION */
		int sum = a+b;
		if (sum >= 10 && sum <= 19)
			return 20;
		else
			return sum;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
