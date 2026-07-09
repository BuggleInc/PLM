package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class In3050Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(in3050((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean in3050(int a, int b)
  {
    /* BEGIN SOLUTION */
    return (a > 29 && a < 41 && b > 29 && b < 41) || (a > 39 && a < 51 && b > 39 && b < 51);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
