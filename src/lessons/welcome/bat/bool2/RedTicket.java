/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class RedTicket extends BatExercise {
	public RedTicket(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("redTicket");
		myWorld.addTest(VISIBLE, 2, 2, 2) ;
		myWorld.addTest(VISIBLE, 2, 2, 1) ;
		myWorld.addTest(VISIBLE, 0, 0, 0) ;
		myWorld.addTest(INVISIBLE, 2, 0, 0) ;
		myWorld.addTest(INVISIBLE, 1, 1, 1) ;
		myWorld.addTest(INVISIBLE, 1, 2, 1) ;
		myWorld.addTest(INVISIBLE, 1, 2, 0) ;
		myWorld.addTest(INVISIBLE, 0, 2, 2) ;
		myWorld.addTest(INVISIBLE, 1, 2, 2) ;
		myWorld.addTest(INVISIBLE, 0, 2, 0) ;
		myWorld.addTest(INVISIBLE, 1, 1, 2) ;

                setup(myWorld);
        }
}
