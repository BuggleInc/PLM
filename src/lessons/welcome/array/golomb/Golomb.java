package lessons.welcome.array.golomb;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Golomb extends BatExercise {
  public Golomb(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("golomb");
    myWorld.addTest(VISIBLE, (Object) new Integer(1));
    myWorld.addTest(VISIBLE, (Object) new Integer(2));
    myWorld.addTest(VISIBLE, (Object) new Integer(3));
    myWorld.addTest(VISIBLE, (Object) new Integer(4));
    myWorld.addTest(VISIBLE, (Object) new Integer(5));
    myWorld.addTest(VISIBLE, (Object) new Integer(6));
    myWorld.addTest(VISIBLE, (Object) new Integer(7));
    myWorld.addTest(VISIBLE, (Object) new Integer(8));
    myWorld.addTest(VISIBLE, (Object) new Integer(9));
    myWorld.addTest(VISIBLE, (Object) new Integer(10));
    myWorld.addTest(VISIBLE, (Object) new Integer(11));
    myWorld.addTest(VISIBLE, (Object) new Integer(12));
    myWorld.addTest(VISIBLE, (Object) new Integer(13));
    myWorld.addTest(INVISIBLE, (Object) new Integer(14));
    myWorld.addTest(INVISIBLE, (Object) new Integer(15));
    myWorld.addTest(INVISIBLE, (Object) new Integer(16));
    myWorld.addTest(INVISIBLE, (Object) new Integer(17));
    myWorld.addTest(INVISIBLE, (Object) new Integer(18));
    myWorld.addTest(INVISIBLE, (Object) new Integer(19));
    myWorld.addTest(INVISIBLE, (Object) new Integer(20));

    setup(myWorld);
  }
}
