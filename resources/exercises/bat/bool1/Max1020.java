package bat.bool1;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Max1020 extends ExerciseTemplated {
	public Max1020(FileUtils fileUtils) {
		super("Max1020");

		BatWorld myWorld = new BatWorld(fileUtils, "max1020");
		myWorld.addTest(BatTest.VISIBLE, 11, 19) ;
		myWorld.addTest(BatTest.VISIBLE, 19, 11) ;
		myWorld.addTest(BatTest.VISIBLE, 11, 9) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 21) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 21) ;
		myWorld.addTest(BatTest.INVISIBLE, 21, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 11) ;
		myWorld.addTest(BatTest.INVISIBLE, 23, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 20, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 7, 20) ;
		myWorld.addTest(BatTest.INVISIBLE, 17, 16) ;

		setup(myWorld);
	}
}
