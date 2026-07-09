package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class ParotTroubleEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(parotTrouble((Boolean)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean parotTrouble(boolean talking, int hour)
  {
    /* BEGIN SOLUTION */
    return (talking && (hour < 7 || hour > 20));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
