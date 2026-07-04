package lessons.welcome.array.arraycount9;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class ArrayCount9 extends BatExercise {
	public ArrayCount9(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("arrayCount9");
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 9}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 9, 9}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 9, 9, 3, 9}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {4, 2, 4, 3, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {9, 2, 4, 3, 1}) ;

                setup(myWorld);
        }
}
