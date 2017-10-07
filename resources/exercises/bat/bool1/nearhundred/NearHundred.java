package bat.bool1.nearhundred;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class NearHundred extends ExerciseTemplated {

	public NearHundred() {
		super("NearHundred", "NearHundred");

		BatWorld myWorld = new BatWorld("nearHundred");
		myWorld.addTest(BatTest.VISIBLE, 93);
		myWorld.addTest(BatTest.VISIBLE, 90);
		myWorld.addTest(BatTest.VISIBLE, 89);

		myWorld.addTest(BatTest.INVISIBLE, 110);
		myWorld.addTest(BatTest.INVISIBLE, 191);
		myWorld.addTest(BatTest.INVISIBLE, 189);
		myWorld.addTest(BatTest.INVISIBLE, 200);
		myWorld.addTest(BatTest.INVISIBLE, 210);
		myWorld.addTest(BatTest.INVISIBLE, 211);
		myWorld.addTest(BatTest.INVISIBLE, -100);

		setup(myWorld);
	}
}
