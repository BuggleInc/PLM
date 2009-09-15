package lessons.bat.string1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class Array123 extends BatExercise {
  public Array123(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[10];
    myWorlds[0] = new BatWorld(VISIBLE, (Object)new int[] {1, 1, 2, 3, 1}) ;
    myWorlds[1] = new BatWorld(VISIBLE, (Object)new int[] {1, 1, 2, 4, 1}) ;
    myWorlds[2] = new BatWorld(VISIBLE, (Object)new int[] {1, 1, 2, 1, 2, 3}) ;
    myWorlds[3] = new BatWorld(INVISIBLE, (Object)new int[] {1, 1, 2, 1, 2, 1}) ;
    myWorlds[4] = new BatWorld(INVISIBLE, (Object)new int[] {1, 2, 3, 1, 2, 3}) ;
    myWorlds[5] = new BatWorld(INVISIBLE, (Object)new int[] {1, 2, 3}) ;
    myWorlds[6] = new BatWorld(INVISIBLE, (Object)new int[] {1, 1, 1}) ;
    myWorlds[7] = new BatWorld(INVISIBLE, (Object)new int[] {1, 2}) ;
    myWorlds[8] = new BatWorld(INVISIBLE, (Object)new int[] {1}) ;
    myWorlds[9] = new BatWorld(INVISIBLE, (Object)new int[] {}) ;

    setup(myWorlds,"array123");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = array123((int[])w.getParameter(0));
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
