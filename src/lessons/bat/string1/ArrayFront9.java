package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class ArrayFront9 extends BatExercise {
  public ArrayFront9(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("arrayFront9");
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 9, 3, 4}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 3, 4, 9}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 3, 4, 5}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {9, 2, 3}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 9, 9}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 9}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {5, 5}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {2}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {9}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {3, 9, 2, 3, 3}) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( arrayFront9((int[])t.getParameter(0)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean arrayFront9(int[] nums) {
  /* BEGIN SOLUTION */
  // First figure the end for the loop
  int end = nums.length;
  if (end > 4) end = 4;
  
  for (int i=0; i<end; i++) {
    if (nums[i] == 9) return true;
  }
  
  return false;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
