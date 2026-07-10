package lessons.welcome.array.search;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class AverageValueEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(averageValue((int[]) param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  int averageValue(int[] nums)
  {
    /* BEGIN SOLUTION */
    int total = 0;
    for (int i = 0; i < nums.length; i++)
      total += nums[i];
    return total / nums.length;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
