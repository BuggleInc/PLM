package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class LastDigit extends ExerciseTemplated {
	public LastDigit(Lesson lesson, FileUtils fileUtils) {
		super("LastDigit");

		BatWorld myWorld = new BatWorld(fileUtils, "lastDigit");
		myWorld.addTest(BatTest.VISIBLE, 7, 17) ;
		myWorld.addTest(BatTest.VISIBLE, 6, 17) ;
		myWorld.addTest(BatTest.VISIBLE, 3, 113) ;
		myWorld.addTest(BatTest.INVISIBLE, 114, 113) ;
		myWorld.addTest(BatTest.INVISIBLE, 114, 4) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 11, 0) ;

		setup(myWorld);
	}
}
