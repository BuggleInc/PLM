package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class In3050 extends BatExercise {

  public In3050(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("in3050");
    myWorld.addTest(VISIBLE, 30, 31);
    myWorld.addTest(VISIBLE, 30, 41);
    myWorld.addTest(VISIBLE, 40, 50);

    myWorld.addTest(INVISIBLE, 40, 51);
    myWorld.addTest(INVISIBLE, 39, 50);
    myWorld.addTest(INVISIBLE, 50, 39);
    myWorld.addTest(INVISIBLE, 40, 39);
    myWorld.addTest(INVISIBLE, 49, 48);
    myWorld.addTest(INVISIBLE, 50, 40);
    myWorld.addTest(INVISIBLE, 50, 51);
    myWorld.addTest(INVISIBLE, 35, 36);
    myWorld.addTest(INVISIBLE, 35, 45);

    setup(myWorld);
  }
}
