package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class PosNeg extends ExerciseTemplated {

	public PosNeg(FileUtils fileUtils) {
		super("PosNeg", "PosNeg");

		BatWorld myWorld = new BatWorld(fileUtils, "posNeg");
		myWorld.addTest(BatTest.VISIBLE, -1,1,false);
		myWorld.addTest(BatTest.VISIBLE, 1,-1,false);
		myWorld.addTest(BatTest.VISIBLE, 1,1,false);

		myWorld.addTest(BatTest.INVISIBLE, -1,-1,false);
		myWorld.addTest(BatTest.INVISIBLE, 1,-1,true);
		myWorld.addTest(BatTest.INVISIBLE, -1,1,true);
		myWorld.addTest(BatTest.INVISIBLE, 1,1,true);
		myWorld.addTest(BatTest.INVISIBLE, -1,-1,true);
		myWorld.addTest(BatTest.INVISIBLE, 5,-5,true);
		myWorld.addTest(BatTest.INVISIBLE, -6,6,false);
		myWorld.addTest(BatTest.INVISIBLE, -5,-5,false);
		myWorld.addTest(BatTest.INVISIBLE, -5,5,true);
		myWorld.addTest(BatTest.INVISIBLE, -5,-5,true);

		setup(myWorld);
	}
}
