package array.arrayfront9;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ArrayFront9 extends ExerciseTemplated {
	public ArrayFront9(Lesson lesson, FileUtils fileUtils) {
		super("ArrayFront9");

		BatWorld myWorld = new BatWorld(fileUtils, "arrayFront9");
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 2, 9, 3, 4}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 2, 3, 4, 9}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 2, 3, 4, 5}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {9, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 9, 9}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 9}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {5, 5}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {2}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {9}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {3, 9, 2, 3, 3}) ;

		setup(myWorld);
	}
}
