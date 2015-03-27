package lessons.bat.string1;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringTimes extends BatExercise {
	public StringTimes(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "stringTimes");
		myWorld.addTest(VISIBLE, "Hi", 2) ;
		myWorld.addTest(VISIBLE, "Hi", 3) ;
		myWorld.addTest(VISIBLE, "Hi", 1) ;
		myWorld.addTest(INVISIBLE, "Hi", 0) ;
		myWorld.addTest(INVISIBLE, "Oh Boy!", 2) ;
		myWorld.addTest(INVISIBLE, "x", 4) ;
		myWorld.addTest(INVISIBLE, "", 4) ;
		myWorld.addTest(INVISIBLE, "code", 2) ;
		myWorld.addTest(INVISIBLE, "code", 3) ;

		templatePython("stringTimes", new String[]{"String","Int"},
				"def stringTimes(str, n):\n",
				"  res = \"\"\n"+
				"  for i in range(n):\n"+
				"    res += str\n"+
				"  return res\n");
		templateScala("stringTimes", new String[]{"String","Int"}, 
				"def stringTimes(str:String, n:Int):String = {\n",
				"  var res = \"\"\n"+
				"  for (i <- 1 to n)\n"+
				"    res ++= str\n"+
				"  return res\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( stringTimes((String)t.getParameter(0), (Integer)t.getParameter(1)) );
		/* END SKEL */
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
