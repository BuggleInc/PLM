package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class TeenSumEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(teenSum((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  int teenSum(int a, int b)
  {
    /* BEGIN SOLUTION */
    if ((a >= 13 && a <= 19) || (b >= 13 && b <= 19))
      return 19;
    else
      return a + b;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
