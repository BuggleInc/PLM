package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class Has271 extends BatExercise {
  public Has271(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("has271");
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 7, 1}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 8, 1}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {2, 7, 1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {3, 8, 2}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, 3}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, 4}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, -1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, -2}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {4, 5, 3, 8, 0}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, 5, 10, 4}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, -2, 4, 9, 3}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, 5, 10, 1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, -2, 4, 10, 2}) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( has271((int[])t.getParameter(0)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean has271(int[] nums) {
  /* BEGIN SOLUTION */
  // Iterate < length-2, so can use i+1 and i+2 in the loop.
  // Return true immediately when seeing 271.
  for (int i=0; i < (nums.length-2); i++) {
    int val = nums[i];
    if (nums[i+1] == (val + 5) &&
      Math.abs(nums[i+2] - (val-1)) <= 2)  return true;
  }
  
  // If we get here ... none found.
  return false;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
