package array.search;

import java.util.Random;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class AverageValue extends ExerciseTemplated {

	public AverageValue(Lesson lesson, FileUtils fileUtils) {
		super("AverageValue");
		
		int[] randomTab = new int[30];
		Random r = new Random();
		for (int i=0; i<randomTab.length; i++) 
			randomTab[i] = r.nextInt(35);
		
		BatWorld myWorld = new BatWorld(fileUtils, "averageValue");
		myWorld.addTest(BatTest.VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 });
		myWorld.addTest(BatTest.VISIBLE, randomTab);
		myWorld.addTest(BatTest.VISIBLE, new int[] {1, 1, 2, 3, 1}) ;
		myWorld.addTest(BatTest.VISIBLE, new int[] {1, 1, 2, 4, 1}) ;
		myWorld.addTest(BatTest.VISIBLE, new int[] {1, 1, 2, 1, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {1, 1, 2, 1, 2, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {1, 2, 3, 1, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {1, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {1, 1, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {1, 2}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {42}) ;

		setup(myWorld);		
	}
}
