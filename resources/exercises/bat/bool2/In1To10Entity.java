/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class In1To10Entity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( in1To10((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
	}

	/* BEGIN TEMPLATE */
	boolean in1To10(int n, boolean outsideMode) {
		/* BEGIN SOLUTION */
		return (outsideMode && (n <= 1 || n >= 10)) || ((! outsideMode) && (n >= 1 && n <= 10));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
