package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class ArrayCount9 extends BatExercise {
  public ArrayCount9(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[7];
    myWorlds[0] = new BatWorld(VISIBLE, (Object)new int[] {1, 2, 9}) ;
    myWorlds[1] = new BatWorld(VISIBLE, (Object)new int[] {1, 9, 9}) ;
    myWorlds[2] = new BatWorld(VISIBLE, (Object)new int[] {1, 9, 9, 3, 9}) ;
    myWorlds[3] = new BatWorld(INVISIBLE, (Object)new int[] {1, 2, 3}) ;
    myWorlds[4] = new BatWorld(INVISIBLE, (Object)new int[] {}) ;
    myWorlds[5] = new BatWorld(INVISIBLE, (Object)new int[] {4, 2, 4, 3, 1}) ;
    myWorlds[6] = new BatWorld(INVISIBLE, (Object)new int[] {9, 2, 4, 3, 1}) ;

    setup(myWorlds,"arrayCount9");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = arrayCount9((int[])w.getParameter(0));
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
