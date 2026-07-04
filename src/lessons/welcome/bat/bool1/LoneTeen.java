package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class LoneTeen extends BatExercise {

	public LoneTeen(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("loneTeen");
		myWorld.addTest(VISIBLE,  13,42);
		myWorld.addTest(VISIBLE,  21,19);
		myWorld.addTest(VISIBLE,  13,13);

		myWorld.addTest(INVISIBLE, 14,20);
		myWorld.addTest(INVISIBLE, 20,15);
		myWorld.addTest(INVISIBLE, 16,17);
		myWorld.addTest(INVISIBLE, 16,9);
		myWorld.addTest(INVISIBLE, 16,18);
		myWorld.addTest(INVISIBLE, 13,19);
		myWorld.addTest(INVISIBLE, 13,20);
		myWorld.addTest(INVISIBLE, 6,18);
		myWorld.addTest(INVISIBLE, 42,13);
		myWorld.addTest(INVISIBLE, 42,42);

                setup(myWorld);
        }
}
