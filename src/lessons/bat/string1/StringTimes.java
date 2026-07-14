package lessons.bat.string1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class StringTimes extends BatExercise {
  public StringTimes(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("stringTimes");
    myWorld.addTest(VISIBLE, "Hi", 2);
    myWorld.addTest(VISIBLE, "Hi", 3);
    myWorld.addTest(VISIBLE, "Hi", 1);
    myWorld.addTest(INVISIBLE, "Hi", 0);
    myWorld.addTest(INVISIBLE, "Oh Boy!", 2);
    myWorld.addTest(INVISIBLE, "x", 4);
    myWorld.addTest(INVISIBLE, "", 4);
    myWorld.addTest(INVISIBLE, "code", 2);
    myWorld.addTest(INVISIBLE, "code", 3);

    setup(myWorld);
  }
}
