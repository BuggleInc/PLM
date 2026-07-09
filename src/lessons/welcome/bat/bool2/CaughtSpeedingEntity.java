package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class CaughtSpeedingEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(caughtSpeeding((Integer)param[0], (Boolean)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  int caughtSpeeding(int speed, boolean isBirthday)
  {
    /* BEGIN SOLUTION */
    if ((isBirthday && speed <= 65) || (speed <= 60))
      return 0;
    else if ((isBirthday && speed <= 85) || (speed <= 80))
      return 1;
    else
      return 2;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
