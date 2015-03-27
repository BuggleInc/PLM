package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Close10 extends BatExercise {

	public Close10(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "close10");

		myWorld.addTest(VISIBLE,  8,13);
		myWorld.addTest(VISIBLE,  13,8);
		myWorld.addTest(VISIBLE,  13,7);

		myWorld.addTest(INVISIBLE, 7,13);
		myWorld.addTest(INVISIBLE, 5,21);
		myWorld.addTest(INVISIBLE, 0,20);
		myWorld.addTest(INVISIBLE, 10,10);

		templatePython("close10", new String[] {"Int","Int"},
				"import math\ndef close10(a, b):\n",
				"   if math.fabs(10-a) == math.fabs(10-b):\n"+
				"      return 0\n"+
				"   elif math.fabs(10-a) < math.fabs(10-b):\n"+
				"      return a\n"+
				"   else:\n"+
				"      return b\n");
		templateScala("close10",new String[] {"Int","Int"},
				"def close10(a:Int, b:Int): Int = {\n",
				"   if (Math.abs(10-a) == Math.abs(10-b))\n"+
				"      return 0\n"+
				"   else if (Math.abs(10-a) < Math.abs(10-b))\n"+
				"      return a\n"+
				"   else\n"+
				"      return b\n"+
				"}");
		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult(close10((Integer)t.getParameter(0),(Integer)t.getParameter(1)));
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int close10(int a, int b) {
		/* BEGIN SOLUTION */
		if (Math.abs(a-10)==Math.abs(b-10))
			return 0;
		if (Math.abs(a-10)<Math.abs(b-10))
			return a;
		return b;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
