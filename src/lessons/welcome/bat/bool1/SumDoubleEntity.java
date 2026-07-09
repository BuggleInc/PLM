package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class SumDoubleEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(sumDouble((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  int sumDouble(int a, int b)
  {
    /* BEGIN SOLUTION */
    if (a == b)
      return (a + b) * 2;
    return a + b;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
