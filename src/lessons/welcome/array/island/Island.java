package lessons.welcome.array.island;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Island extends BatExercise {

  public Island(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("island");
    myWorld.addTest(VISIBLE, (Object) new int[] {0, 1, 2, 2, 1, 0, 1, 2, 2, 1, 0});
    myWorld.addTest(VISIBLE, (Object) new int[] {0, 1, 2, 3, 4, 3, 2, 1, 2, 3, 4, 3, 2, 1, 0});
    myWorld.addTest(INVISIBLE, (Object) new int[] {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0});
    myWorld.addTest(INVISIBLE, (Object) new int[] {0, 0, 0, 0, 0, 0});
    myWorld.addTest(INVISIBLE, (Object) new int[] {0, 0, 2, 4, 0, 0});

    setup(myWorld);
  }
}
