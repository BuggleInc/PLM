package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class TeaPartyEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(teaParty((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  int teaParty(int tea, int candy)
  {
    /* BEGIN SOLUTION */
    if (tea < 5 || candy < 5)
      return 0;
    else if (tea >= 2 * candy || candy >= 2 * tea)
      return 2;
    else // (tea >= 5 && candy >= 5)
      return 1;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
