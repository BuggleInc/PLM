package lessons.bat.string1;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringSplosion extends BatExercise {
	public StringSplosion(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "stringSplosion");
		myWorld.addTest(VISIBLE, "Code") ;
		myWorld.addTest(VISIBLE, "abc") ;
		myWorld.addTest(VISIBLE, "x") ;
		myWorld.addTest(INVISIBLE, "There") ;
		myWorld.addTest(INVISIBLE, "Bye") ;
		myWorld.addTest(INVISIBLE, "Good") ;
		myWorld.addTest(INVISIBLE, "Bad") ;

		templatePython("stringSplosion", new String[]{"String"},
				"def stringSplosion(str):\n",
				"  res = ''\n" +
				"  for i in range(len(str)):\n" +
				"    res += str[0:i+1]\n" +
				"  return res\n");
		templateScala("stringSplosion", new String[]{"String"}, 
				"def stringSplosion(str:String):String = {\n",
				"  var res = \"\"\n" +
				"  for (i <- 0 to str.length-1) \n" +
				"    res ++= str.substring(0,i+1)\n" +
				"  return res\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( stringSplosion((String)t.getParameter(0)) ); 
		/* END SKEL */
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
