package lessons.bat.string1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class StringSplosion extends BatExercise {
  public StringSplosion(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("stringSplosion");
    myWorld.addTest(VISIBLE, "Code");
    myWorld.addTest(VISIBLE, "abc");
    myWorld.addTest(VISIBLE, "x");
    myWorld.addTest(INVISIBLE, "There");
    myWorld.addTest(INVISIBLE, "Bye");
    myWorld.addTest(INVISIBLE, "Good");
    myWorld.addTest(INVISIBLE, "Bad");

    setup(myWorld);
  }
}
