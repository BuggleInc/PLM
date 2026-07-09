package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class HasTeenEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(hasTeen((Integer)param[0], (Integer)param[1], (Integer)param[2])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean hasTeen(int a, int b, int c)
  {
    /* BEGIN SOLUTION */
    return a > 12 && a < 20 || b > 12 && b < 20 || c > 12 && c < 20;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
