package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class LastDigit extends BatExercise {
	public LastDigit(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("lastDigit");
		myWorld.addTest(VISIBLE, 7, 17) ;
		myWorld.addTest(VISIBLE, 6, 17) ;
		myWorld.addTest(VISIBLE, 3, 113) ;
		myWorld.addTest(INVISIBLE, 114, 113) ;
		myWorld.addTest(INVISIBLE, 114, 4) ;
		myWorld.addTest(INVISIBLE, 10, 0) ;
		myWorld.addTest(INVISIBLE, 11, 0) ;

                setup(myWorld);
        }
}
