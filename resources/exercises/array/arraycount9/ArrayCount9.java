package array.arraycount9;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ArrayCount9 extends ExerciseTemplated {
	public ArrayCount9(Lesson lesson) {
		super("ArrayCount9");

		BatWorld myWorld = new BatWorld("arrayCount9");
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 2, 9}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 9, 9}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 9, 9, 3, 9}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {4, 2, 4, 3, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {9, 2, 4, 3, 1}) ;

		setup(myWorld);
	}
}
