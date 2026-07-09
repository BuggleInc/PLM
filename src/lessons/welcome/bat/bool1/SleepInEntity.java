package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class SleepInEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(sleepIn((Boolean)param[0], (Boolean)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean sleepIn(boolean weekday, boolean vacation)
  {
    /* BEGIN SOLUTION */
    return !weekday || vacation;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
