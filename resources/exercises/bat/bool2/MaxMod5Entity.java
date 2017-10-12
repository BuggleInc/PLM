/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class MaxMod5Entity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( maxMod5((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
	}

	/* BEGIN TEMPLATE */
	int maxMod5(int a, int b) {
		/* BEGIN SOLUTION */
		if (a == b)
			return 0;
		else if (a > b)
			if (a % 5 == b % 5)
				return b;
			else 
				return a;
		else 
			if (a % 5 == b % 5)
				return a;
			else 
				return b;  
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
