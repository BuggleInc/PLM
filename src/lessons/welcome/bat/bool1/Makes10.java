package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Makes10 extends BatExercise {

	public Makes10(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "makes10");
		myWorld.addTest(VISIBLE,  9,10);
		myWorld.addTest(VISIBLE,  9,9);
		myWorld.addTest(VISIBLE,  1,9);

		myWorld.addTest(INVISIBLE, 10,1);
		myWorld.addTest(INVISIBLE, 10,10);
		myWorld.addTest(INVISIBLE, 8,2);
		myWorld.addTest(INVISIBLE, 8,3);
		myWorld.addTest(INVISIBLE, 10,42);
		myWorld.addTest(INVISIBLE, 12,-2);

		templatePython("makes10", new String[]{"Int","Int"},
				"def makes10(a, b):\n",
				"   return a==10 or b==10 or (a+b)==10");
		templateScala("makes10", new String[]{"Int","Int"},
				"def makes10(a:Int, b:Int):Boolean = {\n",
				"   return (a==10) || (b==10) || ((a+b)==10)\n"
			  + "}");
		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( makes10((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean makes10(int a, int b) {
		/* BEGIN SOLUTION */
		return a==10||b==10||(a+b)==10;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
