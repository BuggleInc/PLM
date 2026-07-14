package lessons.welcome.array.search;

import java.util.Random;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class IndexOfMaxValue extends BatExercise {

  public IndexOfMaxValue(Lesson lesson)
  {
    super(lesson);
    Random r = new Random();

    int[] tab = new int[15];
    for (int i = 0; i < tab.length; i++)
      tab[i] = r.nextInt(35);

    int[] tab2 = new int[25];
    for (int i = 0; i < tab2.length; i++)
      tab2[i] = r.nextInt(35);

    BatWorld myWorld = new BatWorld("indexOfMaxValue");
    myWorld.addTest(VISIBLE, new int[] {2, -3, 1, 17, -13, 5, 3, 1, 9, 18});
    myWorld.addTest(VISIBLE, tab);
    myWorld.addTest(INVISIBLE, tab2);
    myWorld.addTest(INVISIBLE, new int[] {-4, -3, -1, -17, -13, -5, -3, -1, -9, -18});

    setup(myWorld);
  }
}
