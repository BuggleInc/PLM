package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class NearTenEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(nearTen((Integer)param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean nearTen(int num)
  {
    /* BEGIN SOLUTION */
    return (num % 10) <= 2 || (num % 10) >= 8;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
