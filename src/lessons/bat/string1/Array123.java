package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class Array123 extends BatExercise {
  public Array123(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("array123");
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 2, 3, 1}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 2, 4, 1}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 2, 1, 2, 3}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1, 2, 1, 2, 1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3, 1, 2, 3}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1, 1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {}) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( array123((int[])t.getParameter(0)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean array123(int[] nums) {
  /* BEGIN SOLUTION */
  // Note: iterate < length-2, so can use i+1 and i+2 in the loop
  for (int i=0; i < (nums.length-2); i++) {
    if (nums[i]==1 && nums[i+1]==2 && nums[i+2]==3) return true;
  }
  return false;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
