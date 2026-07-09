package lessons.welcome.bat.bool1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class IcyHotEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(icyHot((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean icyHot(int temp1, int temp2)
  {

    /* BEGIN SOLUTION */
    return temp1 < 0 && temp2 > 100 || temp1 > 100 && temp2 < 0;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
