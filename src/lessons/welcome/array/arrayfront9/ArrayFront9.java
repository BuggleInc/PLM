package lessons.welcome.array.arrayfront9;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class ArrayFront9 extends BatExercise {
  public ArrayFront9(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("arrayFront9");
    myWorld.addTest(VISIBLE, (Object) new int[] {1, 2, 9, 3, 4});
    myWorld.addTest(VISIBLE, (Object) new int[] {1, 2, 3, 4, 9});
    myWorld.addTest(VISIBLE, (Object) new int[] {1, 2, 3, 4, 5});
    myWorld.addTest(INVISIBLE, (Object) new int[] {9, 2, 3});
    myWorld.addTest(INVISIBLE, (Object) new int[] {1, 9, 9});
    myWorld.addTest(INVISIBLE, (Object) new int[] {1, 2, 3});
    myWorld.addTest(INVISIBLE, (Object) new int[] {1, 9});
    myWorld.addTest(INVISIBLE, (Object) new int[] {5, 5});
    myWorld.addTest(INVISIBLE, (Object) new int[] {2});
    myWorld.addTest(INVISIBLE, (Object) new int[] {9});
    myWorld.addTest(INVISIBLE, (Object) new int[] {});
    myWorld.addTest(INVISIBLE, (Object) new int[] {3, 9, 2, 3, 3});

    setup(myWorld);
  }
}
