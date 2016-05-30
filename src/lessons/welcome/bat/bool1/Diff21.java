package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Diff21 extends BatExercise {

	public Diff21(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "diff21");
		myWorld.addTest(VISIBLE,  2);
		myWorld.addTest(VISIBLE,  11);
		myWorld.addTest(VISIBLE,  0);

		myWorld.addTest(INVISIBLE, 19);
		myWorld.addTest(INVISIBLE, 10);
		myWorld.addTest(INVISIBLE, 21);
		myWorld.addTest(INVISIBLE, 22);
		myWorld.addTest(INVISIBLE, 25);
		myWorld.addTest(INVISIBLE, 30);
		myWorld.addTest(INVISIBLE, -21);

		templatePython("diff21", new String[] {"Integer"},
				"def diff21(n):\n",
				"   if (n>21):\n"+
				"      return 2*(n-21)\n"+
				"   return 21-n\n");
		templateScala("diff21", new String[] {"Integer"}, 
				"def diff21(n:Int): Int = {\n",
				"  if (n>21)\n"
			  + "    return 2*(n-21)\n"
			  + "  return 21-n\n"
			  + "}");

		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( diff21((Integer)t.getParameter(0)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int diff21(int n) {
		/* BEGIN SOLUTION */
		if (n>21)
			return 2*(n-21);
		return 21-n;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
