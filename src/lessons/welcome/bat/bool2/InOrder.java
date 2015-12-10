/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class InOrder extends BatExercise {
	public InOrder(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "inOrder");
		myWorld.addTest(VISIBLE, 1, 2, 4, false) ;
		myWorld.addTest(VISIBLE, 1, 2, 1, false) ;
		myWorld.addTest(VISIBLE, 1, 1, 2, true) ;
		myWorld.addTest(INVISIBLE, 3, 2, 4, false) ;
		myWorld.addTest(INVISIBLE, 2, 3, 4, false) ;
		myWorld.addTest(INVISIBLE, 3, 2, 4, true) ;
		myWorld.addTest(INVISIBLE, 4, 2, 2, true) ;
		myWorld.addTest(INVISIBLE, 4, 5, 2, true) ;
		myWorld.addTest(INVISIBLE, 2, 4, 6, true) ;
		myWorld.addTest(INVISIBLE, 7, 9, 10, false) ;
		myWorld.addTest(INVISIBLE, 7, 5, 6, true) ;
		myWorld.addTest(INVISIBLE, 7, 5, 4, true) ;

		templatePython("inOrder", new String[] {"Int","Int","Int","Boolean"},
				"def inOrder(a, b, c, bOk):\n",
				"		return (bOk or (b > a)) and (c > b)\n");
		templateScala("inOrder",new String[] {"Int","Int","Int","Boolean"}, 
				"def inOrder(a:Int, b:Int, c:Int, bOk:Boolean):Boolean = {\n",
				"		return (bOk || (b > a)) && (c > b)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( inOrder((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2), (Boolean)t.getParameter(3)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean inOrder(int a, int b, int c, boolean bOk) {
		/* BEGIN SOLUTION */
		return (bOk || (b > a)) && (c > b);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
