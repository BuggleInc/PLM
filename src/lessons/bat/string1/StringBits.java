package lessons.bat.string1;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringBits extends BatExercise {
	public StringBits(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "stringBits");
		myWorld.addTest(VISIBLE, "Hello") ;
		myWorld.addTest(VISIBLE, "Hi") ;
		myWorld.addTest(VISIBLE, "HiHiHi") ;
		myWorld.addTest(INVISIBLE, "") ;
		myWorld.addTest(INVISIBLE, "Greetings") ;

		templatePython("stringBits", new String[] {"String"},
				"def stringBits(str):\n",
				"  res = ''\n" +
				"  for i in range(0,len(str),2):\n" +
				"    res += str[i:i+1]\n" +
				"  return res\n");
		templateScala("stringBits", new String[] {"String"}, 
				"def stringBits(str:String):String = {\n",
				"  var res:String = \"\"\n" +
				"  for (i <- 0 to str.length-1 by 2)\n" +
				"    res += str.substring(i,i+1)\n" +
				"  return res\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( stringBits((String)t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	String stringBits(String str) {
		/* BEGIN SOLUTION */
		String result = "";
		// Note: the loop increments i by 2
		for (int i=0; i<str.length(); i+=2) {
			result = result + str.substring(i, i+1);
			// Alternately could use str.charAt(i)
		}
		return result;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
