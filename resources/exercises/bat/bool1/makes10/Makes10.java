package bat.bool1.makes10;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Makes10 extends ExerciseTemplated {

	public Makes10() {
		super("Makes10", "Makes10");

		BatWorld myWorld = new BatWorld("makes10");
		myWorld.addTest(BatTest.VISIBLE,  9,10);
		myWorld.addTest(BatTest.VISIBLE,  9,9);
		myWorld.addTest(BatTest.VISIBLE,  1,9);

		myWorld.addTest(BatTest.INVISIBLE, 10,1);
		myWorld.addTest(BatTest.INVISIBLE, 10,10);
		myWorld.addTest(BatTest.INVISIBLE, 8,2);
		myWorld.addTest(BatTest.INVISIBLE, 8,3);
		myWorld.addTest(BatTest.INVISIBLE, 10,42);
		myWorld.addTest(BatTest.INVISIBLE, 12,-2);

		setup(myWorld);
	}
}
