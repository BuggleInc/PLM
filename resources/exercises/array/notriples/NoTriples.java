package array.notriples;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class NoTriples extends ExerciseTemplated {
	public NoTriples(Lesson lesson) {
		super("NoTriples");

		BatWorld myWorld = new BatWorld("noTriples");
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 1, 2, 2, 1}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 1, 2, 2, 2, 1}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {1, 1, 1, 2, 2, 2, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 1, 2, 2, 1, 2, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 2, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 1, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {}) ;

		setup(myWorld);
	}
}
