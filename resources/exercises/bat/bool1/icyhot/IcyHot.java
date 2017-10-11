package bat.bool1.icyhot;


import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class IcyHot extends ExerciseTemplated {

	public IcyHot() {
		super("IcyHot", "IcyHot");

		BatWorld myWorld = new BatWorld("icyHot");
		myWorld.addTest(BatTest.VISIBLE, 120,-1);
		myWorld.addTest(BatTest.VISIBLE, -1,120);
		myWorld.addTest(BatTest.VISIBLE, 2,120);

		myWorld.addTest(BatTest.INVISIBLE, -1,100);
		myWorld.addTest(BatTest.INVISIBLE, -2,-2);
		myWorld.addTest(BatTest.INVISIBLE, 120,120);

		setup(myWorld);
	}
}
