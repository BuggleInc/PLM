package lessons.bat.string1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class StringYak extends BatExercise {
  public StringYak(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("stringYak");
    myWorld.addTest(VISIBLE, "yakpak");
    myWorld.addTest(VISIBLE, "pakyak");
    myWorld.addTest(VISIBLE, "yak123ya");
    myWorld.addTest(INVISIBLE, "yak");
    myWorld.addTest(INVISIBLE, "yakxxxyak");
    myWorld.addTest(INVISIBLE, "HiyakHi");
    myWorld.addTest(INVISIBLE, "xxxyakyyyakzzz");

    setup(myWorld);
  }
}
