package array.golomb;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Golomb extends ExerciseTemplated {
	public Golomb(FileUtils fileUtils) {
		super("Golomb");

		BatWorld myWorld = new BatWorld(fileUtils, "golomb");
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(1)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(2)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(3)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(4)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(5)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(6)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(7)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(8)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(9)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(10)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(11)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(12)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(13)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(14)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(15)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(16)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(17)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(18)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(19)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(20)) ;
		
		setup(myWorld);
	}
}

