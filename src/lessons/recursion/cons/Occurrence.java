package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsExercise;
import lessons.recursion.cons.universe.ConsWorld;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatWorld;

public class Occurrence extends ConsExercise {

	public Occurrence(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new ConsWorld("occurences");
                myWorld.addTest(VISIBLE, new int[] {1, 2, 2, 4}, 1);
                myWorld.addTest(VISIBLE, new int[] {1, 2, 2, 4}, 2);
                myWorld.addTest(VISIBLE, new int[] {1, 2, 3, 4}, 42);
                myWorld.addTest(VISIBLE, new int[] {1, 1, 1}, 1);
                myWorld.addTest(VISIBLE, new int[] {1, 2, 1, 3, 2}, 2);
                myWorld.addTest(INVISIBLE, new int[] {2, 4, 6, 8, 10}, -2);
                myWorld.addTest(INVISIBLE, new int[] {}, 42);

                setup(myWorld);
        }
}
