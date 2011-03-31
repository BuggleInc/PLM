package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class NoTriples extends BatExercise {
  public NoTriples(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[9];
    myWorlds[0] = new BatWorld(VISIBLE, (Object)new int[] {1, 1, 2, 2, 1}) ;
    myWorlds[1] = new BatWorld(VISIBLE, (Object)new int[] {1, 1, 2, 2, 2, 1}) ;
    myWorlds[2] = new BatWorld(VISIBLE, (Object)new int[] {1, 1, 1, 2, 2, 2, 1}) ;
    myWorlds[3] = new BatWorld(INVISIBLE, (Object)new int[] {1, 1, 2, 2, 1, 2, 1}) ;
    myWorlds[4] = new BatWorld(INVISIBLE, (Object)new int[] {1, 2, 1}) ;
    myWorlds[5] = new BatWorld(INVISIBLE, (Object)new int[] {1, 1, 1}) ;
    myWorlds[6] = new BatWorld(INVISIBLE, (Object)new int[] {1, 1}) ;
    myWorlds[7] = new BatWorld(INVISIBLE, (Object)new int[] {1}) ;
    myWorlds[8] = new BatWorld(INVISIBLE, (Object)new int[] {}) ;

    setup(myWorlds,"noTriples");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = noTriples((int[])w.getParameter(0));
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
