package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class PosNegEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(posNeg((Integer)param[0], (Integer)param[1], (Boolean)param[2])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean posNeg(int a, int b, boolean negative)
  {

    /* BEGIN SOLUTION */
    if (negative)
      return a < 0 && b < 0;
    return (a < 0 && b > 0) || (a > 0 && b < 0);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
