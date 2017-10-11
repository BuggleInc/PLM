package bat.bool1.posneg;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class PosNeg extends ExerciseTemplated {

	public PosNeg() {
		super("PosNeg", "PosNeg");

		BatWorld myWorld = new BatWorld("posNeg");
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
