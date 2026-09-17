package lessons.welcome.array.golomb;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Golomb extends BatExercise {
  public Golomb(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("golomb");
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(1));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(2));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(3));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(4));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(5));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(6));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(7));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(8));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(9));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(10));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(11));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(12));
    myWorld.addTest(VISIBLE, (Object)Integer.valueOf(13));
    myWorld.addTest(INVISIBLE, (Object)Integer.valueOf(14));
    myWorld.addTest(INVISIBLE, (Object)Integer.valueOf(15));
    myWorld.addTest(INVISIBLE, (Object)Integer.valueOf(16));
    myWorld.addTest(INVISIBLE, (Object)Integer.valueOf(17));
    myWorld.addTest(INVISIBLE, (Object)Integer.valueOf(18));
    myWorld.addTest(INVISIBLE, (Object)Integer.valueOf(19));
    myWorld.addTest(INVISIBLE, (Object)Integer.valueOf(20));

    setup(myWorld);
  }
}
