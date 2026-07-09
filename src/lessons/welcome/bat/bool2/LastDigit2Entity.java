package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class LastDigit2Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(lastDigit((Integer)param[0], (Integer)param[1], (Integer)param[2])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean lastDigit(int a, int b, int c)
  {
    /* BEGIN SOLUTION */
    int da = a % 10;
    int db = b % 10;
    int dc = c % 10;
    return da == db || da == dc || dc == db;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
