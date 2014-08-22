/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class FizzBuzz extends BatExercise {
	public FizzBuzz(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("fizzBuzz");
		myWorld.addTest(VISIBLE, 2) ;
		myWorld.addTest(VISIBLE, 3) ;
		myWorld.addTest(VISIBLE, 4) ;
		myWorld.addTest(VISIBLE, 5) ;
		myWorld.addTest(VISIBLE, 6) ;
		myWorld.addTest(VISIBLE, 7) ;
		myWorld.addTest(VISIBLE, 8) ;
		myWorld.addTest(VISIBLE, 9) ;
		myWorld.addTest(INVISIBLE, 10) ;
		myWorld.addTest(INVISIBLE, 11) ;
		myWorld.addTest(INVISIBLE, 12) ;
		myWorld.addTest(INVISIBLE, 15) ;
		myWorld.addTest(INVISIBLE, 16) ;
		myWorld.addTest(INVISIBLE, 18) ;

		templatePython("fizzBuzz", 
				"def fizzBuzz(a):\n",
				"	if (a%5 == 0 and a%3 ==0):\n"+
				"		return \"FizzBuzz\"\n"+
				"	elif (a%5 == 0):\n"+
				"		return \"Fizz\"\n"+
				"	elif (a%3 == 0):\n"+
				"		return \"Buzz\"\n"+
				"	return str(a)\n");
		templateScala("fizzBuzz", new String[]{"Int"},
				"def fizzBuzz(a:Int):String = {\n",
				"	(a%5, a%3) match {"+
				"	    case (0,0) => return \"FizzBuzz\"\n"+
				"	    case (_,0) => return \"Buzz\"\n"+
				" 	    case (0,_) => return \"Fizz\"\n"+
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
			return "FizzBuzz";
		else if (a%5 ==0)
			return "Fizz";
		else if (a%3 == 0)
			return "Buzz";
		return ""+a;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
