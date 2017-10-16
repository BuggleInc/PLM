package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class HasTeen extends ExerciseTemplated {

	public HasTeen(FileUtils fileUtils) {
		super("HasTeen");

		BatWorld myWorld = new BatWorld(fileUtils, "hasTeen");
		myWorld.addTest(BatTest.VISIBLE,  13,20,10);		
		myWorld.addTest(BatTest.VISIBLE,  20,19,10);
		myWorld.addTest(BatTest.VISIBLE,  20,10,13);

		myWorld.addTest(BatTest.INVISIBLE, 1,20,12);
		myWorld.addTest(BatTest.INVISIBLE, 19,20,12);
		myWorld.addTest(BatTest.INVISIBLE, 12,20,19);
		myWorld.addTest(BatTest.INVISIBLE, 12,9,20);
		myWorld.addTest(BatTest.INVISIBLE, 12,18,20);
		myWorld.addTest(BatTest.INVISIBLE, 14,2,20);
		myWorld.addTest(BatTest.INVISIBLE, 4,2,20);
		myWorld.addTest(BatTest.INVISIBLE, 11,22,22);

		setup(myWorld);
	}
}
