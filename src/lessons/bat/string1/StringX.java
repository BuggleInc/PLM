package lessons.bat.string1;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringX extends BatExercise {
	public StringX(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "stringX");
		myWorld.addTest(VISIBLE, "xxHxix") ;
		myWorld.addTest(VISIBLE, "abxxxcd") ;
		myWorld.addTest(VISIBLE, "xabxxxcdx") ;
		myWorld.addTest(INVISIBLE, "xKittenx") ;
		myWorld.addTest(INVISIBLE, "Hello") ;
		myWorld.addTest(INVISIBLE, "xx") ;
		myWorld.addTest(INVISIBLE, "x") ;
		myWorld.addTest(INVISIBLE, "") ;

		templatePython("stringX", new String[] {"String"},
				"def stringX(str):\n",
				"  res = ''\n" +
				"  for i in range(len(str)):\n" +
				"    if str[i] != 'x' or i == 0 or i == len(str)-1:" +
				"      res += str[i:i+1]\n" +
				"  return res\n");
		templateScala("stringX", new String[] {"String"},
				"def stringX(str:String):String = {\n",
				"  var res = \"\"\n" +
				"  for (i <- 0 to str.length-1)\n" +
				"    if (str(i) != 'x' || i == 0 || i == str.length-1)" +
				"      res += str.substring(i,i+1)\n" +
				"  return res\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( stringX((String)t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	String stringX(String str) {
		/* BEGIN SOLUTION */
		String result = "";
		for (int i=0; i<str.length(); i++) {
			// Only append the char if it is not the "x" case
			if (!(i > 0 && i < (str.length()-1) && str.substring(i, i+1).equals("x"))) {
				result = result + str.substring(i, i+1); // Could use str.charAt(i) here
			}
		}
		return result;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
