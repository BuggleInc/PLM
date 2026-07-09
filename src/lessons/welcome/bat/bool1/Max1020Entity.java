package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class Max1020Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(max1020((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  int max1020(int a, int b)
  {
    /* BEGIN SOLUTION */
    int A = a > b ? a : b;
    int B = a > b ? b : a;
    if (A < 21 && A > 9)
      return A;
    if (B < 21 && B > 9)
      return B;
    return 0;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
