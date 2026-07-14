package lessons.bat.string1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class FrontTimes extends BatExercise {
  public FrontTimes(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("frontTimes");
    myWorld.addTest(VISIBLE, "Chocolate", 2);
    myWorld.addTest(VISIBLE, "Chocolate", 3);
    myWorld.addTest(VISIBLE, "Abc", 3);
    myWorld.addTest(INVISIBLE, "Ab", 4);
    myWorld.addTest(INVISIBLE, "A", 4);
    myWorld.addTest(INVISIBLE, "", 4);
    myWorld.addTest(INVISIBLE, "Abc", 0);

    setup(myWorld);
  }
}
