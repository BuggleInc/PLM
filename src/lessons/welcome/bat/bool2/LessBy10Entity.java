package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class LessBy10Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(lessBy10((Integer)param[0], (Integer)param[1], (Integer)param[2])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean lessBy10(int a, int b, int c)
  {
    /* BEGIN SOLUTION */
    return ((a - b) >= 10) || ((b - a) >= 10) || ((b - c) >= 10) || ((c - b) >= 10) || ((a - c) >= 10) ||
        ((c - a) >= 10);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
