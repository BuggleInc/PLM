package lessons.welcome.array.array667;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class Array667Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(array667(toPrimitive(toArrayOfType(param[0], Integer.class)))));
    }
  }

  /* BEGIN TEMPLATE */
  int array667(int[] nums)
  {
    /* BEGIN SOLUTION */
    int count = 0;
    // Note: iterate to length-1, so can use i+1 in the loop
    for (int i = 0; i < (nums.length - 1); i++) {
      if (nums[i] == 6) {
        if (nums[i + 1] == 6 || nums[i + 1] == 7) {
          count++;
        }
      }
    }
    return count;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
