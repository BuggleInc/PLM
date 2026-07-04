package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsWorld;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class ButNlast extends BatExercise {

  public ButNlast(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new ConsWorld("butNlast");
    myWorld.addTest(VISIBLE, new int[] {1, 2, 3, 4}, 3);
    myWorld.addTest(VISIBLE, new int[] {1, 2, 3, 4}, 2);
    myWorld.addTest(VISIBLE, new int[] {1, 1, 1}, 3);
    myWorld.addTest(VISIBLE, new int[] {1, 2, 1, 3, 2}, 0);
    myWorld.addTest(INVISIBLE, new int[] {2, 4, 6, 8, 10}, 12);
    myWorld.addTest(INVISIBLE, new int[] {6}, 1);
    myWorld.addTest(INVISIBLE, new int[] {}, 1);
    myWorld.addTest(INVISIBLE, new int[] {}, 0);

    setup(myWorld);
  }
}
