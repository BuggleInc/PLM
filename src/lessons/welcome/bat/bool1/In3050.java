package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class In3050 extends BatExercise {

	public In3050(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "in3050");
		myWorld.addTest(VISIBLE,  30,31);
		myWorld.addTest(VISIBLE,  30,41);
		myWorld.addTest(VISIBLE,  40,50);

		myWorld.addTest(INVISIBLE, 40,51);
		myWorld.addTest(INVISIBLE, 39,50);
		myWorld.addTest(INVISIBLE, 50,39);
		myWorld.addTest(INVISIBLE, 40,39);
		myWorld.addTest(INVISIBLE, 49,48);
		myWorld.addTest(INVISIBLE, 50,40);
		myWorld.addTest(INVISIBLE, 50,51);
		myWorld.addTest(INVISIBLE, 35,36);
		myWorld.addTest(INVISIBLE, 35,45);


		templatePython("in3050", new String[] {"Int","Int"},
				"def in3050(a, b):\n",
				"   return (a>29 and a<41 and b>29 and b<41) or (a>39 and a<51 and b>39 and b<51)\n");
		templateScala("in3050", new String[] {"Int","Int"},
				"def in3050(a:Int, b:Int):Boolean = {\n",
				"   return (a>29 && a<41 && b>29 && b<41) || (a>39 && a<51 && b>39 && b<51)\n"+
				"}");
		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( in3050((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean in3050(int a, int b) {
		/* BEGIN SOLUTION */
		return (a>29&&a<41 && b>29&&b<41) || (a>39&&a<51 && b>39&&b<51);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
