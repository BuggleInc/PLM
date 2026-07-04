/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class NearTen extends BatExercise {
	public NearTen(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("nearTen");
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

                setup(myWorld);
        }
}
