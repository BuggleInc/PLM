package bat.bool1.sleepin;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SleepIn extends ExerciseTemplated {

	public SleepIn() {
		super("SleepIn", "SleepIn");
		tabName = "SourceCode";

		BatWorld myWorld = new BatWorld("sleepIn");
		myWorld.addTest(BatTest.VISIBLE,  false,false);
		myWorld.addTest(BatTest.VISIBLE,  true,false);
		myWorld.addTest(BatTest.INVISIBLE, false,true);
		myWorld.addTest(BatTest.INVISIBLE, true,true);

		setup(myWorld);
	}
}
