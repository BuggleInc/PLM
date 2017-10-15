package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SleepIn extends ExerciseTemplated {

	public SleepIn(FileUtils fileUtils) {
		super("SleepIn", "SleepIn");
		tabName = "SourceCode";

		BatWorld myWorld = new BatWorld(fileUtils, "sleepIn");
		myWorld.addTest(BatTest.VISIBLE,  false,false);
		myWorld.addTest(BatTest.VISIBLE,  true,false);
		myWorld.addTest(BatTest.INVISIBLE, false,true);
		myWorld.addTest(BatTest.INVISIBLE, true,true);

		setup(myWorld);
	}
}
