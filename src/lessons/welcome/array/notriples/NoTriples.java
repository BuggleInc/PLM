package lessons.welcome.array.notriples;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class NoTriples extends BatExercise {
  public NoTriples(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("noTriples");
    myWorld.addTest(VISIBLE, (Object) new int[] {1, 1, 2, 2, 1});
    myWorld.addTest(VISIBLE, (Object) new int[] {1, 1, 2, 2, 2, 1});
    myWorld.addTest(VISIBLE, (Object) new int[] {1, 1, 1, 2, 2, 2, 1});
    myWorld.addTest(INVISIBLE, (Object) new int[] {1, 1, 2, 2, 1, 2, 1});
    myWorld.addTest(INVISIBLE, (Object) new int[] {1, 2, 1});
    myWorld.addTest(INVISIBLE, (Object) new int[] {1, 1, 1});
    myWorld.addTest(INVISIBLE, (Object) new int[] {1, 1});
    myWorld.addTest(INVISIBLE, (Object) new int[] {1});
    myWorld.addTest(INVISIBLE, (Object) new int[] {});

    setup(myWorld);
  }
}
