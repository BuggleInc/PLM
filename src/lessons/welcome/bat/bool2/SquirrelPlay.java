/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SquirrelPlay extends BatExercise {
	public SquirrelPlay(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "squirrelPlay");
		myWorld.addTest(VISIBLE, 70, false) ;
		myWorld.addTest(VISIBLE, 95, false) ;
		myWorld.addTest(VISIBLE, 95, true) ;
		myWorld.addTest(INVISIBLE, 90, false) ;
		myWorld.addTest(INVISIBLE, 90, true) ;
		myWorld.addTest(INVISIBLE, 50, false) ;
		myWorld.addTest(INVISIBLE, 50, true) ;
		myWorld.addTest(INVISIBLE, 100, false) ;
		myWorld.addTest(INVISIBLE, 100, true) ;
		myWorld.addTest(INVISIBLE, 105, true) ;
		myWorld.addTest(INVISIBLE, 59, false) ;
		myWorld.addTest(INVISIBLE, 59, true) ;
		myWorld.addTest(INVISIBLE, 60, false) ;

		templatePython("squirrelPlay", new String[]{"Int","Boolean"},
				"def squirrelPlay(temp, isSummer):\n",
				"   return (temp >= 60 and ((isSummer and temp <= 100) or temp <= 90))");
		templateScala("squirrelPlay", new String[]{"Int","Boolean"}, 
				"def squirrelPlay(temp:Int, isSummer:Boolean):Boolean = {\n",
				"   return (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90))\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( squirrelPlay((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean squirrelPlay(int temp, boolean isSummer) {
		/* BEGIN SOLUTION */
		return (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
