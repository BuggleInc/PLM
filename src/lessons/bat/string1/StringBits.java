package lessons.bat.string1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class StringBits extends BatExercise {
  public StringBits(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("stringBits");
    myWorld.addTest(VISIBLE, "Hello");
    myWorld.addTest(VISIBLE, "Hi");
    myWorld.addTest(VISIBLE, "HiHiHi");
    myWorld.addTest(INVISIBLE, "");
    myWorld.addTest(INVISIBLE, "Greetings");

    setup(myWorld);
  }
}
