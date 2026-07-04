package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Max1020 extends BatExercise {
	public Max1020(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("max1020");
		myWorld.addTest(VISIBLE, 11, 19) ;
		myWorld.addTest(VISIBLE, 19, 11) ;
		myWorld.addTest(VISIBLE, 11, 9) ;
		myWorld.addTest(INVISIBLE, 9, 21) ;
		myWorld.addTest(INVISIBLE, 10, 21) ;
		myWorld.addTest(INVISIBLE, 21, 10) ;
		myWorld.addTest(INVISIBLE, 9, 11) ;
		myWorld.addTest(INVISIBLE, 23, 10) ;
		myWorld.addTest(INVISIBLE, 20, 10) ;
		myWorld.addTest(INVISIBLE, 7, 20) ;
		myWorld.addTest(INVISIBLE, 17, 16) ;

                setup(myWorld);
        }
}
