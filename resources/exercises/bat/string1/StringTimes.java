package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringTimes extends ExerciseTemplated {
	public StringTimes(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("stringTimes");
		myWorld.addTest(BatTest.VISIBLE, "Hi", 2) ;
		myWorld.addTest(BatTest.VISIBLE, "Hi", 3) ;
		myWorld.addTest(BatTest.VISIBLE, "Hi", 1) ;
		myWorld.addTest(BatTest.INVISIBLE, "Hi", 0) ;
		myWorld.addTest(BatTest.INVISIBLE, "Oh Boy!", 2) ;
		myWorld.addTest(BatTest.INVISIBLE, "x", 4) ;
		myWorld.addTest(BatTest.INVISIBLE, "", 4) ;
		myWorld.addTest(BatTest.INVISIBLE, "code", 2) ;
		myWorld.addTest(BatTest.INVISIBLE, "code", 3) ;

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
