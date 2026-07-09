package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class In1To10Entity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(in1To10((Integer)param[0], (Boolean)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean in1To10(int n, boolean outsideMode)
  {
    /* BEGIN SOLUTION */
    return (outsideMode && (n <= 1 || n >= 10)) || ((!outsideMode) && (n >= 1 && n <= 10));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
