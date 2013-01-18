package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class ArrayCount9 extends BatExercise {
  public ArrayCount9(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("arrayCount9");
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 9}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 9, 9}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 9, 9, 3, 9}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {4, 2, 4, 3, 1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {9, 2, 4, 3, 1}) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( arrayCount9((int[])t.getParameter(0)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int arrayCount9(int[] nums) {
  /* BEGIN SOLUTION */
  int count = 0;
  for (int i=0; i<nums.length; i++) {
    if (nums[i] == 9) {
      count++;
    }
  }
  return count;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
