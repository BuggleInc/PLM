package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class CigarPartyEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(cigarParty((Integer)param[0], (Boolean)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean cigarParty(int cigars, boolean isWeekend)
  {
    /* BEGIN SOLUTION */
    return (isWeekend && cigars >= 40) || (!isWeekend && (cigars >= 40) && (cigars <= 60));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
