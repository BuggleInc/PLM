package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Diff21 extends ExerciseTemplated {

	public Diff21(FileUtils fileUtils) {
		super("Diff21");

		BatWorld myWorld = new BatWorld(fileUtils, "diff21");
		myWorld.addTest(BatTest.VISIBLE,  2);
		myWorld.addTest(BatTest.VISIBLE,  11);
		myWorld.addTest(BatTest.VISIBLE,  0);

		myWorld.addTest(BatTest.INVISIBLE, 19);
		myWorld.addTest(BatTest.INVISIBLE, 10);
		myWorld.addTest(BatTest.INVISIBLE, 21);
		myWorld.addTest(BatTest.INVISIBLE, 22);
		myWorld.addTest(BatTest.INVISIBLE, 25);
		myWorld.addTest(BatTest.INVISIBLE, 30);
		myWorld.addTest(BatTest.INVISIBLE, -21);

		setup(myWorld);
	}
}
