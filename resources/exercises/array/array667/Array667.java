package array.array667;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Array667 extends ExerciseTemplated {
	public Array667(Lesson lesson, FileUtils fileUtils) {
		super("Array667");

		BatWorld myWorld = new BatWorld(fileUtils, "array667");
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {6, 6, 2}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {6, 6, 2, 6}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {6, 7, 2, 6}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {6, 6, 2, 6, 7}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 6, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {6, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {3, 6, 7, 6}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {3, 6, 6, 7}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {6, 3, 6, 6}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {6, 7, 6, 6}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 2, 3, 5, 6}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 2, 3, 6, 6}) ;

		setup(myWorld);
	}
}
