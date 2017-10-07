package bat.bool1.monkeytrouble;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class MonkeyTrouble extends ExerciseTemplated {

	public MonkeyTrouble() {
		super("MonkeyTrouble", "MonkeyTrouble");

		BatWorld myWorld = new BatWorld("monkeyTrouble");
		myWorld.addTest(BatTest.VISIBLE, true, true);
		myWorld.addTest(BatTest.VISIBLE, false, false);
		myWorld.addTest(BatTest.VISIBLE, true, false);
		myWorld.addTest(BatTest.INVISIBLE, false, true);

		setup(myWorld);
	}


	
}
