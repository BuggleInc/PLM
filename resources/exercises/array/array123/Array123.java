package array.array123;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Array123 extends ExerciseTemplated {
	public Array123(Lesson lesson) {
		super("Array123");

		BatWorld myWorld = new BatWorld("array123");
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 1, 2, 3, 1}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 1, 2, 4, 1}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 1, 2, 1, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 1, 2, 1, 2, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 2, 3, 1, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 1, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 1, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {2, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 2}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {}) ;

		setup(myWorld);
	}
}
