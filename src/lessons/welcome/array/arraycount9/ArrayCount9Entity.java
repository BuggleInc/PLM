package lessons.welcome.array.arraycount9;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class ArrayCount9Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(arrayCount9((int[])param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  int arrayCount9(int[] nums)
  {
    /* BEGIN SOLUTION */
    int count = 0;
    for (int i = 0; i < nums.length; i++) {
      if (nums[i] == 9) {
        count++;
      }
    }
    return count;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
