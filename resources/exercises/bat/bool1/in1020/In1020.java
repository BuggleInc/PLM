package bat.bool1.in1020;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class In1020 extends ExerciseTemplated {

	public In1020() {
		super("In1020", "In1020");

		BatWorld myWorld = new BatWorld("in1020");
		myWorld.addTest(BatTest.VISIBLE,  12,99);
		myWorld.addTest(BatTest.VISIBLE,  21,12);
		myWorld.addTest(BatTest.VISIBLE,  8,99);

		myWorld.addTest(BatTest.INVISIBLE, 99,10);
		myWorld.addTest(BatTest.INVISIBLE, 20,20);
		myWorld.addTest(BatTest.INVISIBLE, 21,21);
		myWorld.addTest(BatTest.INVISIBLE, 9,9);
		myWorld.addTest(BatTest.INVISIBLE, 10,42);
		myWorld.addTest(BatTest.INVISIBLE, 12,-2);

		setup(myWorld);
	}
}
