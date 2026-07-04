package lessons.welcome.array.array667;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Array667 extends BatExercise {
	public Array667(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("array667");
		myWorld.addTest(VISIBLE, (Object)new int[] {6, 6, 2}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {6, 6, 2, 6}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {6, 7, 2, 6}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {6, 6, 2, 6, 7}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 6, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {6, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {3, 6, 7, 6}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {3, 6, 6, 7}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {6, 3, 6, 6}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {6, 7, 6, 6}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3, 5, 6}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3, 6, 6}) ;

                setup(myWorld);
        }
}
