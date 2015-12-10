package lessons.bat.string1;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class FrontTimes extends BatExercise {
	public FrontTimes(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "frontTimes");
		myWorld.addTest(VISIBLE, "Chocolate", 2) ;
		myWorld.addTest(VISIBLE, "Chocolate", 3) ;
		myWorld.addTest(VISIBLE, "Abc", 3) ;
		myWorld.addTest(INVISIBLE, "Ab", 4) ;
		myWorld.addTest(INVISIBLE, "A", 4) ;
		myWorld.addTest(INVISIBLE, "", 4) ;
		myWorld.addTest(INVISIBLE, "Abc", 0) ;

		templatePython("frontTimes", new String[]{"String","Int"},
				"def frontTimes(str, n):\n",
				"  frontLen = 3\n" +
				"  if frontLen > len(str):\n" +
				"    frontLen = len(str)\n" +
				"  front = ''\n" +
				"  if len(str)>0:\n" +
				"    front = str[0:frontLen]\n" +
				"  res = ''\n" +
				"  for i in range(n):" +
				"    res += front\n" +
				"  return res\n");
		templateScala("frontTimes",new String[]{"String","Int"}, 
				"def frontTimes(str:String, n:Int):String = {\n",
				"  var frontLen = 3\n" +
				"  if (frontLen > str.length)\n" +
				"    frontLen = str.length\n" +
				"  var front = \"\"\n" +
				"  if (str.length >0)\n" +
				"    front = str.substring(0,frontLen)\n" +
				"  return front * n\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( frontTimes((String)t.getParameter(0), (Integer)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	String frontTimes(String str, int n) {
		/* BEGIN SOLUTION */
		int frontLen = 3;
		if (frontLen > str.length()) {
			frontLen = str.length();
		}
		String front = str.substring(0, frontLen);

		String result = "";
		for (int i=0; i<n; i++) {
			result = result + front;
		}
		return result;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
