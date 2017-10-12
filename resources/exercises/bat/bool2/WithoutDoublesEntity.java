/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class WithoutDoublesEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( withoutDoubles((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Boolean)t.getParameter(2)) );
	}

	/* BEGIN TEMPLATE */
	int withoutDoubles(int die1, int die2, boolean noDoubles) {
		/* BEGIN SOLUTION */
		if (noDoubles && (die1 == die2)) {
			if (die1 == 6)
				return 1 + die2;
			else 
				return die1 + 1 + die2;
		} else 
			return die1 + die2;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
