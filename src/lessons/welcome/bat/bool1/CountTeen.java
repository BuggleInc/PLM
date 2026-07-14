package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class CountTeen extends BatExercise {

  public CountTeen(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("countTeen");
    myWorld.addTest(VISIBLE, 13, 20, 10, 54);
    myWorld.addTest(VISIBLE, 20, 19, 13, 15);
    myWorld.addTest(VISIBLE, 20, 10, 13, 42);

    myWorld.addTest(INVISIBLE, 1, 20, 12, 54);
    myWorld.addTest(INVISIBLE, 19, 20, 42, 12);
    myWorld.addTest(INVISIBLE, 12, 16, 20, 19);
    myWorld.addTest(INVISIBLE, 42, 12, 9, 20);
    myWorld.addTest(INVISIBLE, 12, 18, 19, 14);
    myWorld.addTest(INVISIBLE, 14, 2, 20, 99);
    myWorld.addTest(INVISIBLE, 4, 11, 2, 20);
    myWorld.addTest(INVISIBLE, 11, 11, 11, 11);
    myWorld.addTest(INVISIBLE, 15, 15, 15, 15);

    setup(myWorld);
  }
}
