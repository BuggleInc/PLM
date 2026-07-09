package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class AlarmClockEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(alarmClock((Integer)param[0], (Boolean)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  String alarmClock(int day, boolean vacation)
  {
    /* BEGIN SOLUTION */
    if (!vacation) {
      if (day >= 1 && day <= 5)
        return "7:00";
      else
        return "10:00";
    } else {
      if (day >= 1 && day <= 5)
        return "10:00";
      else
        return "off";
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
