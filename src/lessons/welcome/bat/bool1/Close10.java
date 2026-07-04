package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Close10 extends BatExercise {

	public Close10(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("close10");

		myWorld.addTest(VISIBLE,  8,13);
		myWorld.addTest(VISIBLE,  13,8);
		myWorld.addTest(VISIBLE,  13,7);

		myWorld.addTest(INVISIBLE, 7,13);
		myWorld.addTest(INVISIBLE, 5,21);
		myWorld.addTest(INVISIBLE, 0,20);
		myWorld.addTest(INVISIBLE, 10,10);

                setup(myWorld);
        }
}
