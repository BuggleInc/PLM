package lessons.welcome.array.has271;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class Has271Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(has271(toPrimitive(toArrayOfType(param[0], Integer.class)))));
    }
  }

  /* BEGIN TEMPLATE */
  boolean has271(int[] nums)
  {
    /* BEGIN SOLUTION */
    // Iterate < length-2, so can use i+1 and i+2 in the loop.
    // Return true immediately when seeing 271.
    for (int i = 0; i < (nums.length - 2); i++) {
      int val = nums[i];
      if (nums[i + 1] == (val + 5) && Math.abs(nums[i + 2] - (val - 1)) <= 2)
        return true;
    }

    // If we get here ... none found.
    return false;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
