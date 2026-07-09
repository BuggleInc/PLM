package lessons.welcome.array.arrayfront9;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class ArrayFront9Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(arrayFront9(toPrimitive(toArrayOfType(param[0], Integer.class)))));
    }
  }

  /* BEGIN TEMPLATE */
  boolean arrayFront9(int[] nums)
  {
    /* BEGIN SOLUTION */
    // First figure the end for the loop
    int end = nums.length;
    if (end > 4)
      end = 4;

    for (int i = 0; i < end; i++) {
      if (nums[i] == 9)
        return true;
    }

    return false;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
