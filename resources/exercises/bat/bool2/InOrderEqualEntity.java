/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class InOrderEqualEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( inOrderEqual((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2), (Boolean)t.getParameter(3)) ); 
	}

	/* BEGIN TEMPLATE */
	boolean inOrderEqual(int a, int b, int c, boolean equalOk) {
		/* BEGIN SOLUTION */
		return (equalOk && ((a <= b) && (b <= c))) || (a < b && b < c);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
