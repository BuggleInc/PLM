/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class SquirrelPlay extends BatExercise {
	public SquirrelPlay(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("squirrelPlay");
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

                setup(myWorld);
        }
}
