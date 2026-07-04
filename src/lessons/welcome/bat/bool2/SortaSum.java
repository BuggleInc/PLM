/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class SortaSum extends BatExercise {
	public SortaSum(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("sortaSum");
		myWorld.addTest(VISIBLE, 3, 4) ;
		myWorld.addTest(VISIBLE, 9, 4) ;
		myWorld.addTest(VISIBLE, 10, 11) ;
		myWorld.addTest(INVISIBLE, 12, -3) ;
		myWorld.addTest(INVISIBLE, -3, 12) ;
		myWorld.addTest(INVISIBLE, 4, 5) ;
		myWorld.addTest(INVISIBLE, 4, 6) ;
		myWorld.addTest(INVISIBLE, 14, 7) ;
		myWorld.addTest(INVISIBLE, 14, 6) ;

                setup(myWorld);
        }
}
