/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class NearTen extends BatExercise {
	public NearTen(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "nearTen");
		myWorld.addTest(VISIBLE, 12) ;
		myWorld.addTest(VISIBLE, 17) ;
		myWorld.addTest(VISIBLE, 19) ;
		myWorld.addTest(INVISIBLE, 21) ;
		myWorld.addTest(INVISIBLE, 6) ;
		myWorld.addTest(INVISIBLE, 10) ;
		myWorld.addTest(INVISIBLE, 11) ;
		myWorld.addTest(INVISIBLE, 12) ;
		myWorld.addTest(INVISIBLE, 13) ;
		myWorld.addTest(INVISIBLE, 54) ;
		myWorld.addTest(INVISIBLE, 155) ;
		myWorld.addTest(INVISIBLE, 158) ;
		myWorld.addTest(INVISIBLE, 3) ;
		myWorld.addTest(INVISIBLE, 1) ;

		templatePython("nearTen", new String[]{"Int"},
				"def nearTen(num):\n",
				"  return (num % 10) <= 2 or (num % 10) >= 8\n");
		templateScala("nearTen", new String[]{"Int"}, 
				"def nearTen(num:Int):Boolean = {\n",
				"  return (num % 10) <= 2 || (num % 10) >= 8\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( nearTen((Integer)t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean nearTen(int num) {
		/* BEGIN SOLUTION */
		return (num % 10) <= 2 || (num % 10) >= 8; 
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
