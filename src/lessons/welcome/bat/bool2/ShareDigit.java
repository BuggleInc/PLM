/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class ShareDigit extends BatExercise {
	public ShareDigit(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("shareDigit");
		myWorld.addTest(VISIBLE, 12, 23) ;
		myWorld.addTest(VISIBLE, 12, 43) ;
		myWorld.addTest(VISIBLE, 12, 44) ;
		myWorld.addTest(INVISIBLE, 23, 12) ;
		myWorld.addTest(INVISIBLE, 23, 39) ;
		myWorld.addTest(INVISIBLE, 23, 19) ;
		myWorld.addTest(INVISIBLE, 30, 90) ;
		myWorld.addTest(INVISIBLE, 30, 91) ;
		myWorld.addTest(INVISIBLE, 55, 55) ;
		myWorld.addTest(INVISIBLE, 55, 44) ;

                setup(myWorld);
        }
}
