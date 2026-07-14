package lessons.welcome.array.search;

import java.util.Random;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class MaxValue extends BatExercise {

  public MaxValue(Lesson lesson)
  {
    super(lesson);
    Random r = new Random();

    int[] tab = new int[15];
    for (int i = 0; i < tab.length; i++)
      tab[i] = r.nextInt(35);

    int[] tab2 = new int[25];
    for (int i = 0; i < tab2.length; i++)
      tab2[i] = r.nextInt(35);

    int[] tab3 = new int[25];
    for (int i = 0; i < tab3.length; i++)
      tab3[i] = r.nextInt(35) - 15;

    int[] tab4 = new int[25];
    for (int i = 0; i < tab4.length; i++)
      tab4[i] = r.nextInt(35) - 15;

    BatWorld myWorld = new BatWorld("maxValue");
    myWorld.addTest(VISIBLE, new int[] {2, -3, 1, 17, -13, 5, 3, 1, 9, 18});
    myWorld.addTest(VISIBLE, tab);
    myWorld.addTest(VISIBLE, tab2);
    myWorld.addTest(INVISIBLE, tab3);
    myWorld.addTest(INVISIBLE, tab4);

    setup(myWorld);
  }
}
