package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class Diff21Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(diff21((Integer)param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  int diff21(int n)
  {
    /* BEGIN SOLUTION */
    if (n > 21)
      return 2 * (n - 21);
    return 21 - n;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
