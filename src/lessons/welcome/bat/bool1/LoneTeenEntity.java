package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class LoneTeenEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(loneTeen((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean loneTeen(int a, int b)
  {
    /* BEGIN SOLUTION */
    boolean teenA = a > 12 && a < 20;
    boolean teenB = b > 12 && b < 20;
    return (teenA && !teenB) || (teenB && !teenA);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
