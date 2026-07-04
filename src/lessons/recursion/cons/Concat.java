package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsWorld;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Concat extends BatExercise {

  public Concat(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new ConsWorld("concat");
    myWorld.addTest(VISIBLE, new int[] {1, 2, 3}, new int[] {11, 12, 13});
    myWorld.addTest(VISIBLE, new int[] {1, 2, 3}, new int[] {1, 1, 1});
    myWorld.addTest(VISIBLE, new int[] {1, 2, 3}, new int[] {});
    myWorld.addTest(VISIBLE, new int[] {1, 2, 1, 3}, new int[] {64, 36});
    myWorld.addTest(INVISIBLE, new int[] {2, 4, 6, 8, 10}, new int[] {72, 35});
    myWorld.addTest(INVISIBLE, new int[] {}, new int[] {3, 5, 8});
    myWorld.addTest(INVISIBLE, new int[] {}, new int[] {});
    myWorld.addTest(INVISIBLE, new int[] {-2, -4, -6, -8, -10}, new int[] {2, 4, 6, 8, 10});

    setup(myWorld);
  }
}
