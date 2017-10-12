package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class CountTeen extends ExerciseTemplated {

	public CountTeen(Lesson lesson) {
		super("CountTeen");

		BatWorld myWorld = new BatWorld("countTeen");
		myWorld.addTest(BatTest.VISIBLE,  13,20,10,54);
		myWorld.addTest(BatTest.VISIBLE,  20,19,13,15);
		myWorld.addTest(BatTest.VISIBLE,  20,10,13,42);

		myWorld.addTest(BatTest.INVISIBLE, 1,20,12,54);
		myWorld.addTest(BatTest.INVISIBLE, 19,20,42,12);
		myWorld.addTest(BatTest.INVISIBLE, 12,16,20,19);
		myWorld.addTest(BatTest.INVISIBLE, 42,12,9,20);
		myWorld.addTest(BatTest.INVISIBLE, 12,18,19,14);
		myWorld.addTest(BatTest.INVISIBLE, 14,2,20,99);
		myWorld.addTest(BatTest.INVISIBLE, 4,11,2,20);
		myWorld.addTest(BatTest.INVISIBLE, 11,11,11,11);
		myWorld.addTest(BatTest.INVISIBLE, 15,15,15,15);

		setup(myWorld);
	}
}
