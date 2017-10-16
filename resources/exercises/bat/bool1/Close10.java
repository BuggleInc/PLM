package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Close10 extends ExerciseTemplated {

	public Close10(Lesson lesson, FileUtils fileUtils) {
		super("Close10");

		BatWorld myWorld = new BatWorld(fileUtils, "close10");

		myWorld.addTest(BatTest.VISIBLE,  8,13);
		myWorld.addTest(BatTest.VISIBLE,  13,8);
		myWorld.addTest(BatTest.VISIBLE,  13,7);

		myWorld.addTest(BatTest.INVISIBLE, 7,13);
		myWorld.addTest(BatTest.INVISIBLE, 5,21);
		myWorld.addTest(BatTest.INVISIBLE, 0,20);
		myWorld.addTest(BatTest.INVISIBLE, 10,10);

		setup(myWorld);
	}
}
