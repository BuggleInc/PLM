package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsExercise;
import lessons.recursion.cons.universe.ConsWorld;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatWorld;

public class Remove extends ConsExercise {

	public Remove(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new ConsWorld("remove");
                myWorld.addTest(VISIBLE, new int[] {1, 2, 3}, 1);
                myWorld.addTest(VISIBLE, new int[] {1, 1, 2}, 1);
                myWorld.addTest(VISIBLE, new int[] {1, 2, 1, 3}, 3);
                myWorld.addTest(INVISIBLE, new int[] {2, 4, 6, 8, 10}, 10);
                myWorld.addTest(INVISIBLE, new int[] {1, 1, 1}, 1);
                myWorld.addTest(INVISIBLE, new int[] {}, 1);
                myWorld.addTest(INVISIBLE, new int[] {-2, -4, -6, -8, -10}, -4);

                setup(myWorld);
        }
}
