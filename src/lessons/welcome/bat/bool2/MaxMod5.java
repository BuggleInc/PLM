/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class MaxMod5 extends BatExercise {
	public MaxMod5(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("maxMod5");
		myWorld.addTest(VISIBLE, 2, 3) ;
		myWorld.addTest(VISIBLE, 6, 2) ;
		myWorld.addTest(VISIBLE, 3, 2) ;
		myWorld.addTest(INVISIBLE, 8, 12) ;
		myWorld.addTest(INVISIBLE, 7, 12) ;
		myWorld.addTest(INVISIBLE, 11, 6) ;
		myWorld.addTest(INVISIBLE, 2, 7) ;
		myWorld.addTest(INVISIBLE, 7, 7) ;
		myWorld.addTest(INVISIBLE, 9, 1) ;
		myWorld.addTest(INVISIBLE, 9, 14) ;
		myWorld.addTest(INVISIBLE, 1, 2) ;

                setup(myWorld);
        }
}
