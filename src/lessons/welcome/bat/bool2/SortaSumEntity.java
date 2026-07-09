package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class SortaSumEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(sortaSum((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  int sortaSum(int a, int b)
  {
    /* BEGIN SOLUTION */
    int sum = a + b;
    if (sum >= 10 && sum <= 19)
      return 20;
    else
      return sum;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
