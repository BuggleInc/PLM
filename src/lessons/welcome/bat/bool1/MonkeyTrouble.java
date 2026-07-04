package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class MonkeyTrouble extends BatExercise {

	public MonkeyTrouble(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("monkeyTrouble");
		myWorld.addTest(VISIBLE, true, true);
		myWorld.addTest(VISIBLE, false, false);
		myWorld.addTest(VISIBLE, true, false);
		myWorld.addTest(INVISIBLE, false, true);

                setup(myWorld);
        }
}
