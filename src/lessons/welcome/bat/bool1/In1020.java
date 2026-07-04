package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class In1020 extends BatExercise {

	public In1020(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("in1020");
		myWorld.addTest(VISIBLE,  12,99);
		myWorld.addTest(VISIBLE,  21,12);
		myWorld.addTest(VISIBLE,  8,99);

		myWorld.addTest(INVISIBLE, 99,10);
		myWorld.addTest(INVISIBLE, 20,20);
		myWorld.addTest(INVISIBLE, 21,21);
		myWorld.addTest(INVISIBLE, 9,9);
		myWorld.addTest(INVISIBLE, 10,42);
		myWorld.addTest(INVISIBLE, 12,-2);

                setup(myWorld);
        }
}
