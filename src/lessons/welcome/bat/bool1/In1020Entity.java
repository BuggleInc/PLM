package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class In1020Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(in1020((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean in1020(int a, int b)
  {
    /* BEGIN SOLUTION */
    return a > 9 && a < 21 || b > 9 && b < 21;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
