package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class LoneTeen extends ExerciseTemplated {

	public LoneTeen(Lesson lesson, FileUtils fileUtils) {
		super("LoneTeen");

		BatWorld myWorld = new BatWorld(fileUtils, "loneTeen");
		myWorld.addTest(BatTest.VISIBLE,  13,42);
		myWorld.addTest(BatTest.VISIBLE,  21,19);
		myWorld.addTest(BatTest.VISIBLE,  13,13);

		myWorld.addTest(BatTest.INVISIBLE, 14,20);
		myWorld.addTest(BatTest.INVISIBLE, 20,15);
		myWorld.addTest(BatTest.INVISIBLE, 16,17);
		myWorld.addTest(BatTest.INVISIBLE, 16,9);
		myWorld.addTest(BatTest.INVISIBLE, 16,18);
		myWorld.addTest(BatTest.INVISIBLE, 13,19);
		myWorld.addTest(BatTest.INVISIBLE, 13,20);
		myWorld.addTest(BatTest.INVISIBLE, 6,18);
		myWorld.addTest(BatTest.INVISIBLE, 42,13);
		myWorld.addTest(BatTest.INVISIBLE, 42,42);

		setup(myWorld);
	}
}
