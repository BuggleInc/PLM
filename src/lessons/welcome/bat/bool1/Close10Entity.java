package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class Close10Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(close10((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  int close10(int a, int b)
  {
    /* BEGIN SOLUTION */
    if (Math.abs(a - 10) == Math.abs(b - 10))
      return 0;
    if (Math.abs(a - 10) < Math.abs(b - 10))
      return a;
    return b;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
