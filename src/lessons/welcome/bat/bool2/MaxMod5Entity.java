package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class MaxMod5Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(maxMod5((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  int maxMod5(int a, int b)
  {
    /* BEGIN SOLUTION */
    if (a == b)
      return 0;
    else if (a > b)
      if (a % 5 == b % 5)
        return b;
      else
        return a;
    else if (a % 5 == b % 5)
      return a;
    else
      return b;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
