package array.has271;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Has271 extends ExerciseTemplated {
	public Has271(Lesson lesson) {
		super("Has271");

		BatWorld myWorld = new BatWorld("has271");
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 2, 7, 1}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 2, 8, 1}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {2, 7, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {3, 8, 2}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {2, 7, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {2, 7, 4}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {2, 7, -1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {2, 7, -2}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {4, 5, 3, 8, 0}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {2, 7, 5, 10, 4}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {2, 7, -2, 4, 9, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {2, 7, 5, 10, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {2, 7, -2, 4, 10, 2}) ;

		setup(myWorld);
	}
}
