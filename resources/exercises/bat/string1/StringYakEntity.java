package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringYakEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( stringYak((String)t.getParameter(0)) );
	}

	/* BEGIN TEMPLATE */
	String stringYak(String str) {
		/* BEGIN SOLUTION */
		String result = "";

		for (int i=0; i<str.length(); i++) {
			// Look for i starting a "yak" -- advance i in that case
			if (i+2<str.length() && str.charAt(i)=='y' && str.charAt(i+2)=='k') {
				i =  i + 2;
			} else { // Otherwise do the normal append
				result = result + str.charAt(i);
			}
		}

		return result;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
