package lessons.welcome.array.search;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class ExtremaEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(extrema(toPrimitive(toArrayOfType(param[0], Integer.class)))));
    }
  }

  /* BEGIN TEMPLATE */
  int extrema(int[] nums)
  {
    /* BEGIN SOLUTION */
    if (nums.length > 0) {
      int min = nums[0];
      int max = nums[0];
      for (int i = 1; i < nums.length; i++) {
        if (nums[i] < min) {
          min = nums[i];
        }
        if (nums[i] > max) {
          max = nums[i];
        }
      }
      return max - min;
    } else {
      return 0;
    }

    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
