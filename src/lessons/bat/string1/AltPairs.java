package lessons.bat.string1;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class AltPairs extends BatExercise {
	public AltPairs(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "altPairs");
		myWorld.addTest(VISIBLE, "kitten") ;
		myWorld.addTest(VISIBLE, "Chocolate") ;
		myWorld.addTest(VISIBLE, "CodingHorror") ;
		myWorld.addTest(INVISIBLE, "yak") ;
		myWorld.addTest(INVISIBLE, "ya") ;
		myWorld.addTest(INVISIBLE, "y") ;
		myWorld.addTest(INVISIBLE, "") ;
		myWorld.addTest(INVISIBLE, "ThisThatTheOther") ;

		templatePython("altPairs", new String[]{"String"},
				"def altPairs(str):\n",
				"  res = ''\n" +
				"  for i in range(0,len(str),4):\n" +
				"    res += str[i:i+2]\n" +
				"  return res\n");
		templateScala("altPairs",new String[]{"String"}, 
				"def altPairs(str:String):String={\n",
				"  var res = \"\"\n" +
				"  for (i <- 0 to (str.length-1) by 4)\n" +
				"    res ++= str.substring(i, Math.min(i+2,str.length))\n" +
				"  return res\n"+
				"}\n");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( altPairs((String)t.getParameter(0)) );
		/* END SKEL */
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
