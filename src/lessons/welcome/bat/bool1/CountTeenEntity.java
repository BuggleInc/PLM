package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class CountTeenEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(countTeen((Integer)param[0], (Integer)param[1], (Integer)param[2], (Integer)param[3])));
    }
  }

  /* BEGIN TEMPLATE */
  int countTeen(int a, int b, int c, int d)
  {
    /* BEGIN SOLUTION */
    int ret = 0;
    if (a > 12 && a < 20)
      ret += 1;
    if (b > 12 && b < 20)
      ret += 1;
    if (c > 12 && c < 20)
      ret += 1;
    if (d > 12 && d < 20)
      ret += 1;
    return ret;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
