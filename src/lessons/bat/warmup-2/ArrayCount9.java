package lessons.bat.bool;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class ArrayCount9 extends BatExercise {
  public ArrayCount9(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[7];
    myWorlds[0] = new BatWorld(VISIBLE, {1, 2, 9}) ;
    myWorlds[1] = new BatWorld(VISIBLE, {1, 9, 9}) ;
    myWorlds[2] = new BatWorld(VISIBLE, {1, 9, 9, 3, 9}) ;
    myWorlds[3] = new BatWorld(INVISIBLE, {1, 2, 3}) ;
    myWorlds[4] = new BatWorld(INVISIBLE, {}) ;
    myWorlds[5] = new BatWorld(INVISIBLE, {4, 2, 4, 3, 1}) ;
    myWorlds[6] = new BatWorld(INVISIBLE, {9, 2, 4, 3, 1}) ;

    setup(myWorlds,"arrayCount9");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = arrayCount9((Integer[])w.getParameter(0));
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
