/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class LastDigit2 extends BatExercise {
	public LastDigit2(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("lastDigit");
		myWorld.addTest(VISIBLE, 23, 19, 13) ;
		myWorld.addTest(VISIBLE, 23, 19, 12) ;
		myWorld.addTest(VISIBLE, 23, 19, 3) ;
		myWorld.addTest(INVISIBLE, 23, 19, 39) ;
		myWorld.addTest(INVISIBLE, 1, 2, 3) ;
		myWorld.addTest(INVISIBLE, 1, 1, 2) ;
		myWorld.addTest(INVISIBLE, 1, 2, 2) ;
		myWorld.addTest(INVISIBLE, 14, 25, 43) ;
		myWorld.addTest(INVISIBLE, 14, 25, 45) ;
		myWorld.addTest(INVISIBLE, 248, 106, 1002) ;
		myWorld.addTest(INVISIBLE, 248, 106, 1008) ;
		myWorld.addTest(INVISIBLE, 10, 11, 20) ;
		myWorld.addTest(INVISIBLE, 0, 11, 0) ;

                setup(myWorld);
        }
}
