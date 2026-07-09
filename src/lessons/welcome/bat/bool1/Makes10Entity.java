package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class Makes10Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(makes10((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean makes10(int a, int b)
  {
    /* BEGIN SOLUTION */
    return a == 10 || b == 10 || (a + b) == 10;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
