package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class MonkeyTroubleEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(monkeyTrouble((Boolean)param[0], (Boolean)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  public boolean monkeyTrouble(boolean aSmile, boolean bSmile)
  {
    /* BEGIN SOLUTION */
    if (aSmile && bSmile) {
      return true;
    }
    if (!aSmile && !bSmile) {
      return true;
    }
    return false;
    // This all can be shortened to just:
    // return ((aSmile && bSmile) || (!aSmile && !bSmile));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
