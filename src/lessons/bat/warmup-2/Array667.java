package lessons.bat.bool;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class Array667 extends BatExercise {
  public Array667(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[13];
    myWorlds[0] = new BatWorld(VISIBLE, {6, 6, 2}) ;
    myWorlds[1] = new BatWorld(VISIBLE, {6, 6, 2, 6}) ;
    myWorlds[2] = new BatWorld(VISIBLE, {6, 7, 2, 6}) ;
    myWorlds[3] = new BatWorld(INVISIBLE, {6, 6, 2, 6, 7}) ;
    myWorlds[4] = new BatWorld(INVISIBLE, {1, 6, 3}) ;
    myWorlds[5] = new BatWorld(INVISIBLE, {6, 1}) ;
    myWorlds[6] = new BatWorld(INVISIBLE, {}) ;
    myWorlds[7] = new BatWorld(INVISIBLE, {3, 6, 7, 6}) ;
    myWorlds[8] = new BatWorld(INVISIBLE, {3, 6, 6, 7}) ;
    myWorlds[9] = new BatWorld(INVISIBLE, {6, 3, 6, 6}) ;
    myWorlds[10] = new BatWorld(INVISIBLE, {6, 7, 6, 6}) ;
    myWorlds[11] = new BatWorld(INVISIBLE, {1, 2, 3, 5, 6}) ;
    myWorlds[12] = new BatWorld(INVISIBLE, {1, 2, 3, 6, 6}) ;

    setup(myWorlds,"array667");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = array667((Integer[])w.getParameter(0));
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
