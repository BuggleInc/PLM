/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class TwoAsOne extends BatExercise {
	public TwoAsOne(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("twoAsOne");
		myWorld.addTest(VISIBLE, 1, 2, 3) ;
		myWorld.addTest(VISIBLE, 3, 1, 2) ;
		myWorld.addTest(VISIBLE, 3, 2, 2) ;
		myWorld.addTest(INVISIBLE, 2, 3, 1) ;
		myWorld.addTest(INVISIBLE, 5, 3, -2) ;
		myWorld.addTest(INVISIBLE, 5, 3, -3) ;
		myWorld.addTest(INVISIBLE, 2, 5, 3) ;
		myWorld.addTest(INVISIBLE, 9, 5, 5) ;
		myWorld.addTest(INVISIBLE, 9, 4, 5) ;
		myWorld.addTest(INVISIBLE, 5, 4, 9) ;
		myWorld.addTest(INVISIBLE, 3, 3, 0) ;
		myWorld.addTest(INVISIBLE, 3, 3, 2) ;

                setup(myWorld);
        }
}
