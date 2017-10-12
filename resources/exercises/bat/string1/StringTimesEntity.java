package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringTimesEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( stringTimes((String)t.getParameter(0), (Integer)t.getParameter(1)) );
	}

	/* BEGIN TEMPLATE */
	String stringTimes(String str, int n) {
		/* BEGIN SOLUTION */
		String result = "";
		for (int i=0; i<n; i++) {
			result = result + str;  // could use += here
		}
		return result;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
