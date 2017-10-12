package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringSplosionEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( stringSplosion((String)t.getParameter(0)) ); 
	}

	/* BEGIN TEMPLATE */
	String stringSplosion(String str) {
		/* BEGIN SOLUTION */
		String result = "";
		// On each iteration, add the substring of the chars 0..i
		for (int i=0; i<str.length(); i++) {
			result = result + str.substring(0, i+1);
		}
		return result;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
