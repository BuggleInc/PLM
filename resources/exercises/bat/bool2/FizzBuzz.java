/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class FizzBuzz extends ExerciseTemplated {
	public FizzBuzz(Lesson lesson) {
		super("FizzBuzz");

		BatWorld myWorld = new BatWorld("fizzBuzz");
		myWorld.addTest(BatTest.VISIBLE, 2) ;
		myWorld.addTest(BatTest.VISIBLE, 3) ;
		myWorld.addTest(BatTest.VISIBLE, 4) ;
		myWorld.addTest(BatTest.VISIBLE, 5) ;
		myWorld.addTest(BatTest.VISIBLE, 6) ;
		myWorld.addTest(BatTest.VISIBLE, 7) ;
		myWorld.addTest(BatTest.VISIBLE, 8) ;
		myWorld.addTest(BatTest.VISIBLE, 9) ;
		myWorld.addTest(BatTest.INVISIBLE, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 11) ;
		myWorld.addTest(BatTest.INVISIBLE, 12) ;
		myWorld.addTest(BatTest.INVISIBLE, 15) ;
		myWorld.addTest(BatTest.INVISIBLE, 16) ;
		myWorld.addTest(BatTest.INVISIBLE, 18) ;

		templatePython("fizzBuzz", new String[]{"Int"},
				"def fizzBuzz(a):\n",
				"	if (a%5 == 0 and a%3 ==0):\n"+
				"		return \"Fizz Buzz\"\n"+
				"	elif (a%5 == 0):\n"+
				"		return \"Buzz\"\n"+
				"	elif (a%3 == 0):\n"+
				"		return \"Fizz\"\n"+
				"	return str(a)\n");
		templateScala("fizzBuzz", new String[]{"Int"},
				"def fizzBuzz(a:Int):String = {\n",
				"	(a%5, a%3) match {"+
				"	    case (0,0) => return \"Fizz Buzz\"\n"+
				"	    case (_,0) => return \"Fizz\"\n"+
				" 	    case (0,_) => return \"Buzz\"\n"+
				"	    case _     => return \"\"+a\n"+
				"    }\n"+
				"}\n");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( fizzBuzz((Integer)t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	String fizzBuzz(int a) {
		/* BEGIN SOLUTION */
		if (a%5 == 0 && a%3 ==0) 
			return "Fizz Buzz";
		else if (a%5 ==0)
			return "Buzz";
		else if (a%3 == 0)
			return "Fizz";
		return ""+a;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
