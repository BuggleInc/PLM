package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class NearHundred extends BatExercise {

	public NearHundred(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "nearHundred");
		myWorld.addTest(VISIBLE, 93);
		myWorld.addTest(VISIBLE, 90);
		myWorld.addTest(VISIBLE, 89);

		myWorld.addTest(INVISIBLE, 110);
		myWorld.addTest(INVISIBLE, 191);
		myWorld.addTest(INVISIBLE, 189);
		myWorld.addTest(INVISIBLE, 200);
		myWorld.addTest(INVISIBLE, 210);
		myWorld.addTest(INVISIBLE, 211);
		myWorld.addTest(INVISIBLE, -100);

		templatePython("nearHundred", new String[]{"Int"},
				"def nearHundred(n):\n",
				"   return (90<=n and n<=110) or (190<=n and n<=210)\n");
		templateScala("nearHundred", new String[]{"Int"},
				"def nearHundred(n:Int): Boolean = {\n",
				"  return (90<=n && n<=110)||(190<=n&&n<=210);\n"
			  + "}");
		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( nearHundred((Integer)t.getParameter(0)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean nearHundred(int n) {

		/* BEGIN SOLUTION */
		return (90<=n && n<=110)||(190<=n&&n<=210);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
