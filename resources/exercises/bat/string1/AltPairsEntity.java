package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class AltPairsEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( altPairs((String)t.getParameter(0)) );
	}

	/* BEGIN TEMPLATE */
	String altPairs(String str) {
		/* BEGIN SOLUTION */
		String result = "";

		// Run i by 4 to hit 0, 4, 8, ...
		for (int i=0; i<str.length(); i += 4) {
			// Append the chars between i and i+2
			int end = i + 2;
			if (end > str.length()) {
				end = str.length();
			}
			result = result + str.substring(i, end);
		}

		return result;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
