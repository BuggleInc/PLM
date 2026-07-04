package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsWorld;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Last extends BatExercise {

  public Last(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new ConsWorld("last");
    myWorld.addTest(VISIBLE, new int[] {1, 2, 3, 4});
    myWorld.addTest(VISIBLE, new int[] {1, 1, 1});
    myWorld.addTest(VISIBLE, new int[] {1, 2, 1, 3, 2});
    myWorld.addTest(INVISIBLE, new int[] {2, 4, 6, 8, 10});
    myWorld.addTest(INVISIBLE, new int[] {6});

    setup(myWorld);
  }
}
