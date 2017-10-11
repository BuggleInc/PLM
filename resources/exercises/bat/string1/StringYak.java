package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringYak extends ExerciseTemplated {
	public StringYak(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("stringYak");
		myWorld.addTest(BatTest.VISIBLE, "yakpak") ;
		myWorld.addTest(BatTest.VISIBLE, "pakyak") ;
		myWorld.addTest(BatTest.VISIBLE, "yak123ya") ;
		myWorld.addTest(BatTest.INVISIBLE, "yak") ;
		myWorld.addTest(BatTest.INVISIBLE, "yakxxxyak") ;
		myWorld.addTest(BatTest.INVISIBLE, "HiyakHi") ;
		myWorld.addTest(BatTest.INVISIBLE, "xxxyakyyyakzzz") ;

		templatePython("stringYak", new String[]{"String"},
				"def stringYak(str):\n",
				"  res = ''\n" +
				"  i=0\n" +
				"  while i<len(str):\n"+
				"    if i+2<len(str)  and str[i] == 'y' and str[i+2]=='k':\n" +
				"      i += 2\n" +
				"    else:\n" +
				"      res += str[i]\n" +
				"    i+=1\n"+
				"  return res\n");
		templateScala("stringYak", new String[]{"String"}, 
				"def stringYak(str:String):String = { \n",
				"  var res = \"\"\n" +
				"  var i=0\n" +
				"  while (i<str.length) {\n"+
				"    if (i+2<str.length  && str(i) == 'y' && str(i+2)=='k')\n" +
				"      i += 2\n" +
				"    else\n" +
				"      res += str(i)\n" +
				"    i+=1\n"+
				"  }\n"+
				"  return res\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( stringYak((String)t.getParameter(0)) );
		/* END SKEL */
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
