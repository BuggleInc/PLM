package lessons.bat.string1;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringMatch extends BatExercise {
	public StringMatch(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "stringMatch");
		myWorld.addTest(VISIBLE, "xxcaazz", "xxbaaz") ;
		myWorld.addTest(VISIBLE, "abc", "abc") ;
		myWorld.addTest(VISIBLE, "abc", "axc") ;
		myWorld.addTest(INVISIBLE, "hello", "he") ;
		myWorld.addTest(INVISIBLE, "he", "hello") ;
		myWorld.addTest(INVISIBLE, "h", "hello") ;
		myWorld.addTest(INVISIBLE, "", "hello") ;
		myWorld.addTest(INVISIBLE, "aabbccdd", "abbbxxd") ;
		myWorld.addTest(INVISIBLE, "aaxxaaxx", "iaxxai") ;
		myWorld.addTest(INVISIBLE, "iaxxai", "aaxxaaxx") ;

		templatePython("stringMatch", new String[]{"String","String"},
				"def stringMatch(a, b):\n",
				"  l = min( len(a), len(b) )\n" +
				"  count = 0\n" +
				"  for i in range(l-1):\n" +
				"    if a[i:i+2] == b[i:i+2]:\n" +
				"      count += 1\n" +
				"  return count\n");
		templateScala("stringMatch", new String[]{"String","String"},
				"def stringMatch(a:String, b:String):Int = {\n",
				"  val l = Math.min( a.length, b.length )\n" +
				"  var count = 0\n" +
				"  for (i <- 0 to l-2)\n" +
				"    if (a.substring(i,i+2) == b.substring(i,i+2))\n" +
				"      count += 1\n" +
				"  return count\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( stringMatch((String)t.getParameter(0), (String)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int stringMatch(String a, String b) {
		/* BEGIN SOLUTION */
		// Figure which string is shorter.
		int len = Math.min(a.length(), b.length());
		int count = 0;

		// Look at both substrings starting at i
		for (int i=0; i<len-1; i++) {
			String aSub = a.substring(i, i+2);
			String bSub = b.substring(i, i+2);
			if (aSub.equals(bSub)) {  // Use .equals() with strings
				count++;
			}
		}

		return count;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
