package lessons.welcome.array.search;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Extrema extends BatExercise {
  public Extrema(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("extrema");
    myWorld.addTest(VISIBLE, (Object) new int[] {1, 2, 7, 1});
    myWorld.addTest(VISIBLE, (Object) new int[] {1});
    myWorld.addTest(VISIBLE, (Object) new int[] {1, 2});
    myWorld.addTest(VISIBLE, (Object) new int[] {1, 2, 3});
    myWorld.addTest(VISIBLE, (Object) new int[] {1, 3});
    myWorld.addTest(INVISIBLE, (Object) new int[] {1, 1, 1, 1, 1, 1, 1, 1});
    myWorld.addTest(INVISIBLE, (Object) new int[] {1, 2, 7, 1, 7, 9, 10, 1});
    myWorld.addTest(INVISIBLE, (Object) new int[] {10, 4, 1, 2, 7, 1, 0});
    myWorld.addTest(INVISIBLE, (Object) new int[] {45, -34, 0, -42, 72, -42, 72});

    setup(myWorld);
  }
}
