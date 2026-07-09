package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class NearHundredEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(nearHundred((Integer)param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean nearHundred(int n)
  {

    /* BEGIN SOLUTION */
    return (90 <= n && n <= 110) || (190 <= n && n <= 210);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
