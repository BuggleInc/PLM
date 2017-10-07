package bat.bool1.sumdouble;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SumDouble extends ExerciseTemplated {

	public SumDouble() {
		super("SumDouble", "SumDouble");

		BatWorld myWorld = new BatWorld("sumDouble");
		myWorld.addTest(BatTest.VISIBLE,  1,2);
		myWorld.addTest(BatTest.VISIBLE,  3,2);
		myWorld.addTest(BatTest.VISIBLE,  2,2);

		myWorld.addTest(BatTest.INVISIBLE, -1,0);
		myWorld.addTest(BatTest.INVISIBLE, 0,0);
		myWorld.addTest(BatTest.INVISIBLE, 0,1);

		setup(myWorld);
	}


	
}
