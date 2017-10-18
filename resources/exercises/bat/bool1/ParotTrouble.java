package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ParotTrouble extends ExerciseTemplated {

	public ParotTrouble(FileUtils fileUtils) {
		super("ParotTrouble");

		BatWorld myWorld = new BatWorld(fileUtils, "parotTrouble");
		myWorld.addTest(BatTest.VISIBLE,  true,6);
		myWorld.addTest(BatTest.VISIBLE,  true,7);
		myWorld.addTest(BatTest.VISIBLE,  false,6);

		myWorld.addTest(BatTest.INVISIBLE, true,21);
		myWorld.addTest(BatTest.INVISIBLE, false,21);
		myWorld.addTest(BatTest.INVISIBLE, true,23);
		myWorld.addTest(BatTest.INVISIBLE, false,23);
		myWorld.addTest(BatTest.INVISIBLE, true,20);

		setup(myWorld);
	}
}
