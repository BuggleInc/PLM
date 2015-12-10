package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class In1020 extends BatExercise {

	public In1020(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "in1020");
		myWorld.addTest(VISIBLE,  12,99);
		myWorld.addTest(VISIBLE,  21,12);
		myWorld.addTest(VISIBLE,  8,99);

		myWorld.addTest(INVISIBLE, 99,10);
		myWorld.addTest(INVISIBLE, 20,20);
		myWorld.addTest(INVISIBLE, 21,21);
		myWorld.addTest(INVISIBLE, 9,9);
		myWorld.addTest(INVISIBLE, 10,42);
		myWorld.addTest(INVISIBLE, 12,-2);

		templatePython("in1020", new String[] {"Int","Int"},
				"def in1020(a, b):\n",
				"   return (a>9 and a<21) or (b>9 and b<21)");
		templateScala("in1020", new String[] {"Int","Int"},
				"def in1020(a:Int, b:Int):Boolean = {\n",
				"   return (a>9 && a<21) || (b>9 && b<21)\n"+
				"}");
		setup(myWorld);
	}


	@Override
	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( in1020((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean in1020(int a, int b) {
		/* BEGIN SOLUTION */
		return a>9&&a<21 || b>9&&b<21;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
