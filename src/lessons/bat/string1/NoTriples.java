package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class NoTriples extends BatExercise {
  public NoTriples(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("noTriples");
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 2, 2, 1}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 2, 2, 2, 1}) ;
    myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 1, 2, 2, 2, 1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1, 2, 2, 1, 2, 1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1, 1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {1}) ;
    myWorld.addTest(INVISIBLE, (Object)new int[] {}) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( noTriples((int[])t.getParameter(0)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean noTriples(int[] nums) {
  /* BEGIN SOLUTION */
  // Iterate < length-2, so can use i+1 and i+2 in the loop.
  // Return false immediately if every seeing a triple.
  for (int i=0; i < (nums.length-2); i++) {
    int first = nums[i];
    if (nums[i+1]==first && nums[i+2]==first) return false;
  }
  
  // If we get here ... no triples.
  return true;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
