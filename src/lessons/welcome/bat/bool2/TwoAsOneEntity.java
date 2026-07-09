package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class TwoAsOneEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(twoAsOne((Integer)param[0], (Integer)param[1], (Integer)param[2])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean twoAsOne(int a, int b, int c)
  {
    /* BEGIN SOLUTION */
    return (a + b == c) || (a + c == b) || (b + c == a);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
