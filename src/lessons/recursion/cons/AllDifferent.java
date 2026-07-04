package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsWorld;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class AllDifferent extends BatExercise {

  public AllDifferent(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new ConsWorld("allDifferent");
    myWorld.addTest(VISIBLE, new int[] {1, 2, 3, 4});
    myWorld.addTest(VISIBLE, new int[] {1, 2, 2, 4});
    myWorld.addTest(VISIBLE, new int[] {1, 1, 1});
    myWorld.addTest(VISIBLE, new int[] {1, 2, 4, 3, 1});
    myWorld.addTest(INVISIBLE, new int[] {2, 4, 6, 8, 10});
    myWorld.addTest(INVISIBLE, new int[] {});

    setup(myWorld);
  }
}
