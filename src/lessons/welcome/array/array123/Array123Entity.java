package lessons.welcome.array.array123;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class Array123Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(array123((int[]) param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean array123(int[] nums)
  {
    /* BEGIN SOLUTION */
    // Note: iterate < length-2, so can use i+1 and i+2 in the loop
    for (int i = 0; i < (nums.length - 2); i++) {
      if (nums[i] == 1 && nums[i + 1] == 2 && nums[i + 2] == 3)
        return true;
    }
    return false;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
