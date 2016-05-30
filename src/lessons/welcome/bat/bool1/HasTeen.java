package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class HasTeen extends BatExercise {

	public HasTeen(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "hasTeen");
		myWorld.addTest(VISIBLE,  13,20,10);		
		myWorld.addTest(VISIBLE,  20,19,10);
		myWorld.addTest(VISIBLE,  20,10,13);

		myWorld.addTest(INVISIBLE, 1,20,12);
		myWorld.addTest(INVISIBLE, 19,20,12);
		myWorld.addTest(INVISIBLE, 12,20,19);
		myWorld.addTest(INVISIBLE, 12,9,20);
		myWorld.addTest(INVISIBLE, 12,18,20);
		myWorld.addTest(INVISIBLE, 14,2,20);
		myWorld.addTest(INVISIBLE, 4,2,20);
		myWorld.addTest(INVISIBLE, 11,22,22);


		templatePython("hasTeen", new String[] {"Int","Int","Int"},
				"def hasTeen(a, b, c):\n",
				"   return (a>12 and a<20) or (b>12 and b<20) or (c>12 and c<20)\n");
		templateScala("hasTeen", new String[] {"Int","Int","Int"}, 
				"def hasTeen(a:Int, b:Int, c:Int): Boolean = {\n",
				"   return (a>12 && a<20) || (b>12 && b<20) || (c>12 && c<20)\n"
		  	  + "}");

		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( hasTeen((Integer)t.getParameter(0),(Integer)t.getParameter(1),(Integer)t.getParameter(2)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean hasTeen(int a, int b, int c) {
		/* BEGIN SOLUTION */
		return a>12&&a<20 || b>12&&b<20 || c>12&&c<20;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
