/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class CaughtSpeeding extends BatExercise {
	public CaughtSpeeding(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "caughtSpeeding");
		myWorld.addTest(VISIBLE, 60, false) ;
		myWorld.addTest(VISIBLE, 65, false) ;
		myWorld.addTest(VISIBLE, 65, true) ;
		myWorld.addTest(INVISIBLE, 80, false) ;
		myWorld.addTest(INVISIBLE, 85, false) ;
		myWorld.addTest(INVISIBLE, 85, true) ;
		myWorld.addTest(INVISIBLE, 70, false) ;
		myWorld.addTest(INVISIBLE, 75, false) ;
		myWorld.addTest(INVISIBLE, 75, true) ;
		myWorld.addTest(INVISIBLE, 40, false) ;
		myWorld.addTest(INVISIBLE, 40, true) ;
		myWorld.addTest(INVISIBLE, 90, false) ;

		templatePython("caughtSpeeding", new String[] {"Int","Boolean"},
				"def caughtSpeeding(speed, isBirthday):\n",
				"	if ((isBirthday and speed <= 65) or (speed <= 60)):\n"+
				"		return 0\n"+
				"	elif ((isBirthday and speed <= 85) or (speed <= 80)):\n"+
				"		return 1\n"+
				"	else:\n"+
				"		return 2\n");
		templateScala("caughtSpeeding",new String[] {"Int","Boolean"}, 
				"def caughtSpeeding(speed:Int, isBirthday:Boolean):Int = {\n",
				"	if ((isBirthday && speed <= 65) || (speed <= 60))\n"+
				"		return 0\n"+
				"	else if ((isBirthday && speed <= 85) || (speed <= 80))\n"+
				"		return 1\n"+
				"	else\n"+
				"		return 2\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( caughtSpeeding((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int caughtSpeeding(int speed, boolean isBirthday) {
		/* BEGIN SOLUTION */
		if ((isBirthday && speed <= 65) || (speed <= 60))
			return 0;
		else if ((isBirthday && speed <= 85) || (speed <= 80))
			return 1;
		else 
			return 2;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
