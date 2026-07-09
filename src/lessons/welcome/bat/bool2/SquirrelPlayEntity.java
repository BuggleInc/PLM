package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class SquirrelPlayEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(squirrelPlay((Integer)param[0], (Boolean)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean squirrelPlay(int temp, boolean isSummer)
  {
    /* BEGIN SOLUTION */
    return (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
