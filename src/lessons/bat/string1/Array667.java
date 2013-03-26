package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class Array667 extends BatExercise {
  public Array667(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("array667");
    myWorld.addTest(VISIBLE, (Object)new int[] {6, 6, 2}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {6, 6, 2, 6}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {6, 7, 2, 6}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {6, 6, 2, 6, 7}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 6, 3}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {6, 1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {3, 6, 7, 6}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {3, 6, 6, 7}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {6, 3, 6, 6}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {6, 7, 6, 6}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3, 5, 6}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3, 6, 6}) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( array667((int[])t.getParameter(0)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int array667(int[] nums) {
  /* BEGIN SOLUTION */
  int count = 0;
  // Note: iterate to length-1, so can use i+1 in the loop
  for (int i=0; i < (nums.length-1); i++) {
    if (nums[i] == 6) {
      if (nums[i+1] == 6 || nums[i+1] == 7) {
        count++;
      }
    }
  }
  return count;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
