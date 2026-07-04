package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class HasTeen extends BatExercise {

	public HasTeen(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("hasTeen");
		myWorld.addTest(VISIBLE,  13,20,10);		
		myWorld.addTest(VISIBLE,  20,19,10);
		myWorld.addTest(VISIBLE,  20,10,13);

		myWorld.addTest(INVISIBLE, 1,20,12);
		myWorld.addTest(INVISIBLE, 19,20,12);
		myWorld.addTest(INVISIBLE, 12,20,19);
		myWorld.addTest(INVISIBLE, 12,9,20);
		myWorld.addTest(INVISIBLE, 12,18,20);
		myWorld.addTest(INVISIBLE, 14,2,20);
		myWorld.addTest(INVISIBLE, 4,2,20);
		myWorld.addTest(INVISIBLE, 11,22,22);

                setup(myWorld);
        }
}
