package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class In3050 extends ExerciseTemplated {

	public In3050(FileUtils fileUtils) {
		super("In3050");

		BatWorld myWorld = new BatWorld(fileUtils, "in3050");
		myWorld.addTest(BatTest.VISIBLE,  30,31);
		myWorld.addTest(BatTest.VISIBLE,  30,41);
		myWorld.addTest(BatTest.VISIBLE,  40,50);

		myWorld.addTest(BatTest.INVISIBLE, 40,51);
		myWorld.addTest(BatTest.INVISIBLE, 39,50);
		myWorld.addTest(BatTest.INVISIBLE, 50,39);
		myWorld.addTest(BatTest.INVISIBLE, 40,39);
		myWorld.addTest(BatTest.INVISIBLE, 49,48);
		myWorld.addTest(BatTest.INVISIBLE, 50,40);
		myWorld.addTest(BatTest.INVISIBLE, 50,51);
		myWorld.addTest(BatTest.INVISIBLE, 35,36);
		myWorld.addTest(BatTest.INVISIBLE, 35,45);

		setup(myWorld);
	}
}
