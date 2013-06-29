package lessons.bat.string1;
import jlm.core.model.Game;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class StringSplosion extends BatExercise {
	public StringSplosion(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("stringSplosion");
		myWorld.addTest(VISIBLE, "Code") ;
		myWorld.addTest(VISIBLE, "abc") ;
		myWorld.addTest(VISIBLE, "x") ;
		myWorld.addTest(INVISIBLE, "There") ;
		myWorld.addTest(INVISIBLE, "Bye") ;
		myWorld.addTest(INVISIBLE, "Good") ;
		myWorld.addTest(INVISIBLE, "Bad") ;

		langTemplate(Game.PYTHON, "stringSplosion", 
				"def stringSplosion(str):\n",
				"  res = ''\n" +
				"  for i in range(len(str)):\n" +
				"    res += str[0:i+1]\n" +
				"  return res\n");
		setup(myWorld);
	}

	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( stringSplosion((String)t.getParameter(0)) ); 
	}
	/* END SKEL */

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
