package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class InOrderEqualEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(inOrderEqual((Integer)param[0], (Integer)param[1], (Integer)param[2], (Boolean)param[3])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean inOrderEqual(int a, int b, int c, boolean equalOk)
  {
    /* BEGIN SOLUTION */
    return (equalOk && ((a <= b) && (b <= c))) || (a < b && b < c);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
