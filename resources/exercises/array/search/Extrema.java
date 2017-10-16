package array.search;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Extrema extends ExerciseTemplated {
	public Extrema(FileUtils fileUtils) {
		super("Extrema");

		BatWorld myWorld = new BatWorld(fileUtils, "extrema");
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 2, 7, 1}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 2}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 2, 3}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 1, 1, 1, 1, 1, 1, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 2, 7, 1, 7, 9, 10, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {10, 4, 1, 2, 7, 1, 0}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {45, -34, 0, -42, 72, -42, 72}) ;
		
		setup(myWorld);
	}
}
