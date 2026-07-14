package lessons.bat.string1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class StringMatch extends BatExercise {
  public StringMatch(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("stringMatch");
    myWorld.addTest(VISIBLE, "xxcaazz", "xxbaaz");
    myWorld.addTest(VISIBLE, "abc", "abc");
    myWorld.addTest(VISIBLE, "abc", "axc");
    myWorld.addTest(INVISIBLE, "hello", "he");
    myWorld.addTest(INVISIBLE, "he", "hello");
    myWorld.addTest(INVISIBLE, "h", "hello");
    myWorld.addTest(INVISIBLE, "", "hello");
    myWorld.addTest(INVISIBLE, "aabbccdd", "abbbxxd");
    myWorld.addTest(INVISIBLE, "aaxxaaxx", "iaxxai");
    myWorld.addTest(INVISIBLE, "iaxxai", "aaxxaaxx");

    setup(myWorld);
  }
}
