package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SumDouble extends ExerciseTemplated {

	public SumDouble(FileUtils fileUtils) {
		super("SumDouble", "SumDouble");

		BatWorld myWorld = new BatWorld(fileUtils, "sumDouble");
		myWorld.addTest(BatTest.VISIBLE,  1,2);
		myWorld.addTest(BatTest.VISIBLE,  3,2);
		myWorld.addTest(BatTest.VISIBLE,  2,2);

		myWorld.addTest(BatTest.INVISIBLE, -1,0);
		myWorld.addTest(BatTest.INVISIBLE, 0,0);
		myWorld.addTest(BatTest.INVISIBLE, 0,1);

		setup(myWorld);
	}


	
}
